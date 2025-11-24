module org.drk.di_ejercicioev_t3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;


    opens org.drk.di_ejercicioev_t3;
    exports org.drk.di_ejercicioev_t3;
    exports org.drk.di_ejercicioev_t3.controllers;
    opens org.drk.di_ejercicioev_t3.controllers;
    exports  org.drk.di_ejercicioev_t3.usuario;
    opens org.drk.di_ejercicioev_t3.usuario;
}