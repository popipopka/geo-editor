import {createGeoObject, getAllGeoObjects} from './api/api.js'
import {addFeatureToMap, createFeatureFromGeoObject, map, vectorSource} from './map/map.js'
import {getDraw, getIsDrawing, getModify, getSelect, setCurrentGeoObject, setDraw, setIsDrawing} from './state.js'
import {GeoObjectsList} from './component/GeoObjectsList.js'

// Модальное окно создания объекта
let createObjectBtn = document.getElementById('create-object-btn')
let cancelCreateBtn = document.getElementById('cancel-create-btn')
let newObjectNameInput = document.getElementById('new-object-name')

let createObjectModal = document.getElementById('create-object-modal')
let tempFeature = null

createObjectBtn.addEventListener('click', createNewObject)
cancelCreateBtn.addEventListener('click', function () {
    if (tempFeature) {
        vectorSource.removeFeature(tempFeature)
        renderGeoObjectsList()
    }
    hideCreateObjectModal()
})

function showCreateObjectModal() {
    newObjectNameInput.value = ''
    createObjectModal.style.display = 'block'
    newObjectNameInput.focus()
}

function hideCreateObjectModal() {
    createObjectModal.style.display = 'none'
    tempFeature = null
}

window.addEventListener('click', function (event) {
    if (event.target === createObjectModal) {
        hideCreateObjectModal()
    }
})


// Отрисовка списка объектов
let geoObjectsListComponent = null

function renderGeoObjectsList() {
    if (!geoObjectsListComponent) {
        const container = document.getElementById('geo-objects-list');
        geoObjectsListComponent = new GeoObjectsList({
            container: container,
            vectorSource: vectorSource
        });
    }
    geoObjectsListComponent.loadAndRender();
}


// Режим рисования
let drawTypeSelect = document.getElementById('draw-type')
let drawBtn = document.getElementById('draw-btn')
let featureInfo = document.getElementById('feature-info')

drawBtn.addEventListener('click', function () {
    if (getIsDrawing()) {
        map.removeInteraction(getDraw())
        setIsDrawing(false)
        drawBtn.textContent = 'Начать рисование'
        drawBtn.className = 'control-btn drawing'
        featureInfo.innerHTML = ''

        // Очищаем все временные слои
        const layers = map.getLayers().getArray()
        layers.forEach(layer => {
            if (layer.get('isDrawLayer')) {
                map.removeLayer(layer)
            }
        })
    } else {
        addDrawInteraction()
    }
})

function addDrawInteraction() {
    map.removeInteraction(getDraw())
    map.removeInteraction(getModify())
    map.removeInteraction(getSelect())

    // Создаем временный источник для отображения процесса рисования
    const drawSource = new ol.source.Vector()
    const drawLayer = new ol.layer.Vector({
        source: drawSource,
        style: new ol.style.Style({
            stroke: new ol.style.Stroke({
                color: '#ff0000',
                width: 2,
                lineDash: [5, 5]
            }),
            fill: new ol.style.Fill({
                color: 'rgba(255, 0, 0, 0.1)'
            }),
            image: new ol.style.Circle({
                radius: 5,
                fill: new ol.style.Fill({
                    color: '#ff0000'
                }),
                stroke: new ol.style.Stroke({
                    color: '#ffffff',
                    width: 2
                })
            })
        })
    })

    // Помечаем слой как временный для рисования
    drawLayer.set('isDrawLayer', true)

    map.addLayer(drawLayer)

    const draw = new ol.interaction.Draw({
        source: drawSource,
        type: drawTypeSelect.value,
        style: new ol.style.Style({
            stroke: new ol.style.Stroke({
                color: '#ff0000',
                width: 2,
                lineDash: [5, 5]
            }),
            fill: new ol.style.Fill({
                color: 'rgba(255, 0, 0, 0.1)'
            }),
            image: new ol.style.Circle({
                radius: 5,
                fill: new ol.style.Fill({
                    color: '#ff0000'
                }),
                stroke: new ol.style.Stroke({
                    color: '#ffffff',
                    width: 2
                })
            })
        })
    })

    setDraw(draw)
    map.addInteraction(draw)

    // Обработка начала рисования
    draw.on('drawstart', function (event) {
        const drawType = drawTypeSelect.value
        let instruction = ''

        switch (drawType) {
            case 'Point':
                instruction = 'Кликните на карте для создания точки'
                break
            case 'LineString':
                instruction = 'Кликайте для добавления точек линии. Дважды кликните для завершения'
                break
            case 'Polygon':
                instruction = 'Кликайте для добавления вершин полигона. Дважды кликните для завершения'
                break
        }

        featureInfo.innerHTML = instruction
    })

    // Обработка процесса рисования
    draw.on('drawend', function (event) {
        let feature = event.feature
        let featureId = Date.now().toString()

        feature.setProperties({
            'id': featureId,
            'name': 'Новый объект',
            'type': drawTypeSelect.value,
            'showLabel': false,
            'isTemporary': true
        })

        // Перемещаем объект из временного источника в основной
        drawSource.removeFeature(feature)
        vectorSource.addFeature(feature)

        tempFeature = feature
        setCurrentGeoObject(feature)
        renderGeoObjectsList()

        // Удаляем временный слой
        map.removeLayer(drawLayer)

        // Показываем модальное окно для ввода названия
        showCreateObjectModal()
    })

    // Обработка отмены рисования
    draw.on('drawabort', function (event) {
        map.removeLayer(drawLayer)
    })

    setIsDrawing(true)
    drawBtn.textContent = 'Закончить рисование'
    drawBtn.className = 'control-btn drawing-active'
}


// Создать новый объект
async function createNewObject() {
    if (!tempFeature || !newObjectNameInput.value.trim()) {
        alert('Пожалуйста, введите название объекта')
        return
    }

    const name = newObjectNameInput.value.trim()
    tempFeature.set('name', name)
    tempFeature.set('isTemporary', false)
    tempFeature.set('showLabel', true)

    const geometry = tempFeature.getGeometry()
    let coords = []
    let type

    if (geometry instanceof ol.geom.Point) {
        type = 'POINT'
        const c = ol.proj.transform(geometry.getCoordinates(), 'EPSG:3857', 'EPSG:4326')
        coords = [{lat: c[1], lon: c[0]}]
    } else if (geometry instanceof ol.geom.LineString) {
        type = 'LINE'
        coords = geometry.getCoordinates().map(c => {
            const lonlat = ol.proj.transform(c, 'EPSG:3857', 'EPSG:4326')
            return {lat: lonlat[1], lon: lonlat[0]}
        })
    } else if (geometry instanceof ol.geom.Polygon) {
        type = 'POLYGON'
        const ring = geometry.getCoordinates()[0]
        coords = ring.map(c => {
            const lonlat = ol.proj.transform(c, 'EPSG:3857', 'EPSG:4326')
            return {lat: lonlat[1], lon: lonlat[0]}
        })
    }

    const payload = {
        name: name,
        type: type,
        coords: coords
    }

    try {
        await createGeoObject(payload)
        hideCreateObjectModal()
        renderGeoObjectsList()
    } catch (e) {
        alert('Ошибка при сохранении объекта на сервер')
        console.error('Ошибка при сохранении объекта на сервер: ', e)
    }
}


// Загрузить объекты
async function loadGeoObjectsFromBackend() {
    try {
        const geoObjects = await getAllGeoObjects()

        vectorSource.clear()
        geoObjects.forEach(geoObject => {
            const feature = createFeatureFromGeoObject(geoObject)
            addFeatureToMap(feature)
        })

        renderGeoObjectsList()

    } catch (e) {
        alert('Ошибка при загрузке объектов с сервера')
        console.error('Ошибка при загрузке объектов с сервера: ', e)
    }
}

loadGeoObjectsFromBackend()

// drawBtn.className = 'control-btn drawing'