package ru.vistar.geoeditor.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vistar.geoeditor.core.dto.GeoObjectData;
import ru.vistar.geoeditor.core.dto.GeoObjectPayload;
import ru.vistar.geoeditor.core.exception.GeoObjectNotFoundException;
import ru.vistar.geoeditor.core.mapper.GeoObjectMapper;
import ru.vistar.geoeditor.core.model.GeoObject;
import ru.vistar.geoeditor.data.mapper.GeoObjectDataMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GeoObjectService {
    private final GeoObjectDataMapper geoObjectDataMapper;
    private final GeoObjectMapper mapper;

    public List<GeoObjectData> getAll() {
        return mapper.toDataList(geoObjectDataMapper.findAll());
    }

    public GeoObjectData getById(Long id) {
        return geoObjectDataMapper.findById(id)
                .map(mapper::toData)
                .orElseThrow(() -> new GeoObjectNotFoundException(id));
    }

    public Long create(GeoObjectPayload payload) {
        GeoObject object = mapper.toModel(payload);

        geoObjectDataMapper.create(object);
        return object.getId();
    }

    @Transactional
    public void update(Long id, GeoObjectPayload payload) {
        GeoObject object = geoObjectDataMapper.findById(id)
                .orElseThrow(() -> new GeoObjectNotFoundException(id));
        mapper.updateFromPayload(object, payload);

        geoObjectDataMapper.update(object);
    }

    @Transactional
    public void delete(Long id) {
        if (!geoObjectDataMapper.existsById(id)) {
            throw new GeoObjectNotFoundException(id);
        }
        geoObjectDataMapper.delete(id);
    }
}
