package com.absencia.diginamic.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class PostEmployerWtrModel {

    @NotNull(message = "La date est requise")
    private LocalDate date;

    @NotBlank(message = "Le motif est requis")
    private String label;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}