package ru.yandex.qatools.allure.repository;

import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

import javax.xml.bind.*;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * @author eroshenkoam@yandex-team.ru
 */
public class XmlConverter implements Converter {

    @Override
    public Object fromBody(TypedInput typedInput, Type type) throws ConversionException {
        try (InputStream stream = typedInput.in()) {
            return JAXB.unmarshal(stream, (Class) type);
        } catch (IOException e) {
            throw new ConversionException(e);
        }
    }

    @Override
    public TypedOutput toBody(Object o) {
        throw new UnsupportedOperationException("Could not convert body back to xml");
    }

}
