import { GeoObjectListItem } from './GeoObjectListItem.js'
import { getCurrentGeoObject } from '../state.js'

export class GeoObjectsList {
    constructor({ container, vectorSource }) {
        this.container = container
        this.vectorSource = vectorSource
    }

    loadAndRender() {
        this.render()
    }

    render() {
        const geoObjects = this.vectorSource.getFeatures()

        // Создаем структуру с фиксированным заголовком и прокручиваемым контентом
        this.container.innerHTML = `
            <h3>Объекты</h3>
            <div class="geo-objects-list-content"></div>
        `
        
        const contentContainer = this.container.querySelector('.geo-objects-list-content')
        
        if (geoObjects.length === 0) {
            contentContainer.innerHTML = '<p>Нет объектов</p>'
            return
        }

        const current = getCurrentGeoObject()

        geoObjects.forEach((geoObject) => {
            const item = new GeoObjectListItem(geoObject, {
                onRender: () => this.render()
            })

            if (current && geoObject.get('id') === current.get('id')) {
                item.setSelected(true)
            } else {
                item.setSelected(false)
            }
            contentContainer.appendChild(item.getElement())
        })
    }
} 