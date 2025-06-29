let currentGeoObject = null;

let isDrawing = false;
let select = null;
let draw = null;
let modify = null;

export function getCurrentGeoObject() { return currentGeoObject; }
export function setCurrentGeoObject(obj) { currentGeoObject = obj; }

export function getIsDrawing() { return isDrawing; }
export function setIsDrawing(val) { isDrawing = val; }

export function getSelect() { return select; }
export function setSelect(val) { select = val; }

export function getDraw() { return draw; }
export function setDraw(val) { draw = val; }

export function getModify() { return modify; }
export function setModify(val) { modify = val; } 