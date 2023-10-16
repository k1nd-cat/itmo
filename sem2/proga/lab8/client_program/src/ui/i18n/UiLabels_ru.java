package ui.i18n;

import java.util.ListResourceBundle;

public class UiLabels_ru extends ListResourceBundle {

    public Object[][] getContents() { return contents; }
    private Object[][] contents = {
            {"app_title", "Управление персоналом"},
            {"tab_login", "Вход для зарегистрированных пользователей"},
            {"tab_register", "У меня нет учетной записи"},
            {"tab_register_title", "Регистрация"},
            {"field_login", "Логин"},
            {"field_password", "Пароль"},
            {"form_error", "Ошибка"},
            {"success", "Выполнено успешно!"},
            {"error_missing_login", "Имя пользователя не указано"},
            {"error_missing_password", "Пароль не указан, либо содержит менее 5 символов"},
            {"error_unknown", "Неизвестная ошибка, повторите попытку позже"},
            {"continue", "Продолжить"},
            {"close", "Закрыть"},
            {"delete", "Удалить"},
            {"form_info", "Информация"},
            {"welcome", "Добро пожаловать "},
            {"user_not_found", "Неправильное имя пользователя или пароль"},
            {"register_error", "Невозможно зарегистрироваться, повторите попытку позже"},
            {"user_already_exists", "Пользователь с таким именем уже существует"},
            {"welcome_new", "Регистрация прошла успешно! Добро пожаловать "},
            {"button_continue", "Продолжить"},
            {"button_cancel", "Нет"},
            {"form_question", "Вопрос" },
            {"button_exit", "Выход из приложения" },
            {"confirm_exit", "Завершить работу?" },
            {"button_add", "Добавить персону" },
            {"button_add_max", "Добавить персону если высота максимальна" },
            {"button_add_min", "Добавить персону если высота минимальна" },
            {"button_table", "Табличный вид"},
            {"button_image", "Диаграмма"},
            {"button_delete", "Удалить запись о персоне"},
            {"remove_greater", "Удалите всех людей, чей рост превышает указанный" },
            {"clear", "Удалить всех людей"},
            {"person_add_title", "Добавление персоны"},
            {"person_edit_title", "Редактирование персоны"},
            {"person_view_title", "Просмотр персоны"},
            {"person_add_intro", "Заполните значения полей и нажмите \"Продолжить\" или \"X\" для отмены операции"},
            {"person_edit_intro", "Измените значения полей и нажмите \"Продолжить\" или \"X\" для отмены операции"},
            {"person_view_intro", "Нажмите \"Закрыть\" или \"X\" для закрытия окна"},
            {"average_height", "Средняя высота"},
            {"field_id", "ИД"},
            {"field_name", "Имя"},
            {"field_height", "Рост"},
            {"field_coords_x", "Координата: X "},
            {"field_coords_y", "Координата: Y"},
            {"field_eye_color", "Цвет глаз"},
            {"field_hair_color", "Цвет волос"},
            {"field_country", "Страна"},
            {"field_location_x", "Место: X"},
            {"field_location_y", "Место: Y"},
            {"field_location_z", "Место: Z"},
            {"field_creation_date", "Дата создания"},
            {"person_added", "Запись добавлена"},
            {"person_updated", "Запись изменена"},
            {"person_not_added_by_condition", "Запись не добавлена по условию"},
            {"person_not_found", "Информация не найдена"},
            {"confirm_delete", "Удалить запись"},
            {"confirm_delete_many", "Удалить несколько записей?"},
            {"person_deleted", "Запись удалена"},
            {"person_edit_denied", "У Вас нет прав на редактирование данной записи"},
            {"choose_script", "Выбрать скрипт"},
            {"error_script", "Неправильный формат скрипта"},
            {"button_info", "Информация о коллекции персон"},
            {"info_text_item1", "Количество персон"},
            {"info_text_item2", "Количество моих персон"},
            {"hair_info", "Показать уникальные цвета волос"},
            {"location_min", "Отображение человека с минимальным местоположением"},

    };

}