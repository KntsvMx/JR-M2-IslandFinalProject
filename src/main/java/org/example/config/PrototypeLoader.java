package org.example.config;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.example.abstraction.annotations.Config;
import org.example.abstraction.interfaces.GameObject;
import org.example.exceptions.InitGameException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;

public class PrototypeLoader {
    private static PrototypeLoader instance;
    private ObjectMapper objectMapper = new YAMLMapper();

    private PrototypeLoader() {

    }

    public static PrototypeLoader getInstance() {
        if(instance == null) {
            instance = new PrototypeLoader();
        }
        return instance;
    }

    public <T> T loadPrototype(@NotNull Class<T> type) {
        if (!type.isAnnotationPresent(Config.class)) {
            throw new IllegalArgumentException(String.format("Prototype class %s must have @Config annotation", type ));
        }
        return loadObject(getConfigFilePath(type), type);
    }

    private <T> T loadObject(@NotNull URL configFilePath, Class<T> type) {
        T gameObject;

        try {
            gameObject = objectMapper.readValue(configFilePath, type);
        } catch (IOException e) {
            String message = String.format("Can't find config file %s for class %s", configFilePath.getFile(), type);
            throw new InitGameException(message, e);
        }

        return gameObject;
    }

    private URL getConfigFilePath(Class<?> type) {
        Config config = type.getAnnotation(Config.class);
        return type.getClassLoader().getResource(config.fileName());
    }
}
