import {setCurrentGeoObject} from '../state.js'
import {doPan, vectorSource, map} from '../map/map.js'
import {deleteGeoObject, updateGeoObject} from '../api/api.js'

export class GeoObjectListItem {
    constructor(geoObject, {onRender} = {}) {
        this.geoObject = geoObject
        this.onRender = onRender
        this.centerCoords = this.calcCenterCoords()
        this.isEditingGeometry = false

        this.createElement()
        this.renderContent()
        this.setupEventListeners()
    }

    calcCenterCoords() {
        const geometry = this.geoObject.getGeometry()
        if (geometry) {
            const extent = geometry.getExtent()
            return ol.extent.getCenter(extent)
        }
        return null
    }

    createElement() {
        this.element = document.createElement('div')
        this.element.className = 'geo-object-list-item'
    }

    renderContent() {
        const name = this.geoObject.get('name') || 'Без имени'
        const type = this.translateType(this.geoObject.get('type'))
        const isTemporary = this.geoObject.get('isTemporary') || false
        
        let centerCoordsInfo = 'Координаты недоступны'
        if (this.centerCoords) {
            const centerLonLat = ol.proj.transform(this.centerCoords, 'EPSG:3857', 'EPSG:4326')
            centerCoordsInfo = centerLonLat[0].toFixed(4) + ', ' + centerLonLat[1].toFixed(4)
        }

        const nameDisplay = isTemporary ? `<em>${name}</em>` : name
        const deleteBtnText = isTemporary ? 'Отменить' : 'Удалить'
        const editBtnText = this.isEditingGeometry ? 'Применить' : 'Изменить'
        const editBtnClass = this.isEditingGeometry ? 'edit-btn applying' : 'edit-btn'

        this.element.innerHTML = `
            <div class="geo-object-content">
                <div class="geo-object-name">${nameDisplay}</div>
                <div class="geo-object-type">${type}</div>
                <div class="geo-object-coords">${centerCoordsInfo}</div>
            </div>
            <div class="geo-object-actions">
                <button class="${editBtnClass}">${editBtnText}</button>
                <button class="delete-btn">${deleteBtnText}</button>
            </div>
        `
    }

    translateType(type) {
        switch(type) {
            case 'POINT':
                return 'Точка'
            case 'LINE':
                return 'Линия'
            case 'POLYGON':
                return 'Многоугольник'
            default:
                return type
        }
    }

    setupEventListeners() {
        this.element.addEventListener('click', (e) => {
            if (e.target.classList.contains('delete-btn') || e.target.classList.contains('edit-btn')) {
                return
            }
            
            setCurrentGeoObject(this.geoObject)
            this.panToObject()

            if (this.onRender) {
                this.onRender()
            }
        })

        const editBtn = this.element.querySelector('.edit-btn')
        editBtn.addEventListener('click', async (e) => {
            e.stopPropagation()
            
            if (this.isEditingGeometry) {
                await this.applyGeometryChanges()
            } else {
                this.startGeometryEditing()
            }
        })

        const deleteBtn = this.element.querySelector('.delete-btn')
        deleteBtn.addEventListener('click', async (e) => {
            e.stopPropagation()
            
            const isTemporary = this.geoObject.get('isTemporary') || false
            const confirmMessage = isTemporary ? 'Отменить создание объекта?' : 'Удалить объект?'
            
            if (confirm(confirmMessage)) {
                try {
                    if (!isTemporary) {
                        await deleteGeoObject(this.geoObject.get('id'))
                    }

                    vectorSource.removeFeature(this.geoObject)

                    if (this.onRender) {
                        this.onRender()
                    }
                } catch (error) {
                    alert('Ошибка при удалении объекта с сервера')
                    console.error('Ошибка при удалении объекта с сервера: ', e)
                }
            }
        })
    }

    startGeometryEditing() {
        const interactions = map.getInteractions().getArray()
        interactions.forEach(interaction => {
            if (interaction instanceof ol.interaction.Select || 
                interaction instanceof ol.interaction.Modify ||
                interaction instanceof ol.interaction.Draw) {
                map.removeInteraction(interaction)
            }
        })

        const featureCollection = new ol.Collection([this.geoObject])

        this.selectInteraction = new ol.interaction.Select({
            features: featureCollection
        })
        map.addInteraction(this.selectInteraction)

        this.modifyInteraction = new ol.interaction.Modify({
            features: featureCollection
        })
        map.addInteraction(this.modifyInteraction)

        this.modifyInteraction.on('modifyend', () => {
            this.centerCoords = this.calcCenterCoords()
            this.renderContent()
            this.setupEventListeners()
        })

        this.isEditingGeometry = true
        this.renderContent()
        this.setupEventListeners()

        setCurrentGeoObject(this.geoObject)
        this.panToObject()
    }

    async applyGeometryChanges() {
        try {
            const geometry = this.geoObject.getGeometry()
            let coords = []

            if (geometry instanceof ol.geom.Point) {
                const c = ol.proj.transform(geometry.getCoordinates(), 'EPSG:3857', 'EPSG:4326')
                coords = [{ lat: c[1], lon: c[0] }]
            } else if (geometry instanceof ol.geom.LineString) {
                coords = geometry.getCoordinates().map(c => {
                    const lonlat = ol.proj.transform(c, 'EPSG:3857', 'EPSG:4326')
                    return { lat: lonlat[1], lon: lonlat[0] }
                })
            } else if (geometry instanceof ol.geom.Polygon) {
                const ring = geometry.getCoordinates()[0]
                coords = ring.map(c => {
                    const lonlat = ol.proj.transform(c, 'EPSG:3857', 'EPSG:4326')
                    return { lat: lonlat[1], lon: lonlat[0] }
                })
            }

            const payload = {
                coords: coords
            }

            await updateGeoObject(this.geoObject.get('id'), payload)

            this.stopGeometryEditing()

            if (this.onRender) {
                this.onRender()
            }
        } catch (e) {
            alert('Ошибка при сохранении изменений объекта на сервер')
            console.log('Ошибка при сохранении изменений объекта на сервер: ' + e)
        }
    }

    stopGeometryEditing() {
        if (this.selectInteraction) {
            map.removeInteraction(this.selectInteraction)
            this.selectInteraction = null
        }
        if (this.modifyInteraction) {
            map.removeInteraction(this.modifyInteraction)
            this.modifyInteraction = null
        }

        this.isEditingGeometry = false
        this.renderContent()
        this.setupEventListeners()
    }

    panToObject() {
        if (this.centerCoords) {
            doPan(this.centerCoords)
        }
    }

    setSelected(selected) {
        if (selected) {
            this.element.classList.add('selected')
        } else {
            this.element.classList.remove('selected')
        }
    }

    getElement() {
        return this.element
    }
} 
