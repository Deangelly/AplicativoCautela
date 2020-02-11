package com.unasusam.appcautela.DAO;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import androidx.room.TypeConverter;

public class Converters {
    @TypeConverter
    public static List<DadosEmprestimo> fromString(String value) {
        Type listType = new TypeToken<List<DadosEmprestimo>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(List<DadosEmprestimo> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
