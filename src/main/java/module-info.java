module org.drk.di_ejercicioev_t3 {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.drk.di_ejercicioev_t3 to javafx.fxml;
    exports org.drk.di_ejercicioev_t3;
}