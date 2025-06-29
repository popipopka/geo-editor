import 'https://cdn.jsdelivr.net/npm/ol@v10.5.0/dist/ol.js';

// --- Источники и слои ---
export const osmLayer = new ol.layer.Tile({
    source: new ol.source.OSM()
});

export const vectorSource = new ol.source.Vector();
export const vectorLayer = new ol.layer.Vector({
    source: vectorSource,
    style: function(feature) {
        var color = '#e2081a';
        var isHighlighted = feature.get('highlighted');
        var width = isHighlighted ? 4 : 2;
        var fillOpacity = isHighlighted ? '90' : '80';
        var radius = isHighlighted ? 9 : 7;
        return new ol.style.Style({
            fill: new ol.style.Fill({
                color: color.replace('#', '#' + fillOpacity)
            }),
            stroke: new ol.style.Stroke({
                color: color,
                width: width
            }),
            image: new ol.style.Circle({
                radius: radius,
                fill: new ol.style.Fill({
                    color: color
                }),
                stroke: new ol.style.Stroke({
                    color: '#ffffff',
                    width: isHighlighted ? 2 : 0
                })
            }),
            text: feature.get('showLabel') ? new ol.style.Text({
                text: feature.get('name'),
                offsetY: -15,
                fill: new ol.style.Fill({
                    color: '#000'
                }),
                stroke: new ol.style.Stroke({
                    color: '#fff',
                    width: 3
                })
            }) : null
        });
    }
});

const centerOfVoronezh = ol.proj.transform([39.199907, 51.660878], 'EPSG:4326', 'EPSG:3857');

const view = new ol.View({
    center: centerOfVoronezh,
    zoom: 12
});

export const map = new ol.Map({
    target: 'map',
    layers: [osmLayer, vectorLayer],
    view: view
});

const mousePosition = new ol.control.MousePosition({
    coordinateFormat: ol.coordinate.createStringXY(4),
    projection: 'EPSG:4326',
    target: document.getElementById('myposition'),
    undefinedHTML: '&nbsp;'
});

map.addControl(mousePosition);

// Переместиться к локации
export function doPan(location) {
    map.getView().animate({
        center: location,
        duration: 500
    });
}

// Создание Feature из GeoObject
export function createFeatureFromGeoObject(geoObject) {
    const coords = geoObject.coords;
    let geometry;

    if (geoObject.type === 'POINT') {
        const point = ol.proj.transform([coords[0].lon, coords[0].lat], 'EPSG:4326', 'EPSG:3857');
        geometry = new ol.geom.Point(point);
    } else if (geoObject.type === 'LINE') {
        const lineCoords = coords.map(c => ol.proj.transform([c.lon, c.lat], 'EPSG:4326', 'EPSG:3857'));
        geometry = new ol.geom.LineString(lineCoords);
    } else if (geoObject.type === 'POLYGON') {
        const polygonCoords = coords.map(c => ol.proj.transform([c.lon, c.lat], 'EPSG:4326', 'EPSG:3857'));
        geometry = new ol.geom.Polygon([polygonCoords]);
    }

    const feature = new ol.Feature({
        geometry: geometry,
        name: geoObject.name,
        type: geoObject.type,
        id: geoObject.id,
        showLabel: true
    });

    return feature;
}

// Добавить Feature на карту
export function addFeatureToMap(feature) {
    vectorSource.addFeature(feature);
    return feature;
}