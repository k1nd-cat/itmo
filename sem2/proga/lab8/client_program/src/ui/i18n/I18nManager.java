package ui.i18n;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class I18nManager {

    // public static String lang = "Русский";
    // public static Locale locale = Locale.forLanguageTag("ru_RU");

    public static String lang = "english";
    // public static Locale locale = Locale.forLanguageTag("EN");
    {
        Locale.setDefault(Locale.forLanguageTag("EN"));
    }

    private ObjectProperty<ResourceBundle> resources = new SimpleObjectProperty<>();

    public ObjectProperty<ResourceBundle> resourcesProperty() {
        return resources;
    }

    public final ResourceBundle getResources() {
        if (resourcesProperty().get() == null) {
            // ResourceBundle i18n = ResourceBundle.getBundle("ui.i18n.UiLabels");
            ResourceBundle i18n = ResourceBundle.getBundle("ui.i18n.labels");
            resourcesProperty().set(i18n);
        }
        return resourcesProperty().get();
    }

    public final void setResources(Locale locale) {
        // ResourceBundle i18n = ResourceBundle.getBundle("ui.i18n.UiLabels");
        Locale.setDefault(locale);
        ResourceBundle i18n = ResourceBundle.getBundle("ui.i18n.labels");
        resourcesProperty().set(i18n);
        // I18nManager.locale = locale;
    }

    public StringBinding getStringBinding(String key) {
        return new StringBinding() {
            { bind(resourcesProperty()); }
            @Override
            public String computeValue() {
                return getResources().getString(key);
            }
        };
    }
}
