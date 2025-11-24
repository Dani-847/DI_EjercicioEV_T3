package org.drk.di_ejercicioev_t3.controllers;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.drk.di_ejercicioev_t3.usuario.Usuario;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private TextField cCorreo;
    @FXML
    private ComboBox<String> cPlataforma;
    @FXML
    private CheckBox cAdministrador;
    @FXML
    private Button btnAñadir;
    @FXML
    private TextField cVersion;
    @FXML
    private TextField cFecha;
    @FXML
    private Label txtLog;

    @FXML
    private TableView<Usuario> tabla;
    @FXML
    private TableColumn<Usuario, String> txtCorreo;
    @FXML
    private TableColumn<Usuario, String> txtPlataforma;
    @FXML
    private TableColumn<Usuario, String> txtAdministrador;
    @FXML
    private TableColumn<Usuario, String> txtVersion;
    @FXML
    private TableColumn<Usuario, String> txtFecha;

    private final ObservableList<Usuario> contactos = FXCollections.observableArrayList();
    private final ObservableList<String> plataformas = FXCollections.observableArrayList(
            "Windows", "Linux", "macOS"
    );

    private final String version = "1.0";
    private static final String PLATAFORMA_POR_DEFECTO = "Windows";

    private final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarComboPlataforma();
        configurarTabla();
        configurarEventos();
        inicializarCamposFijos();
        configurarClickTabla();
    }

    private void configurarComboPlataforma() {
        cPlataforma.setItems(plataformas);
        cPlataforma.setValue(PLATAFORMA_POR_DEFECTO);
    }

    private void configurarTabla() {
        txtCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        txtPlataforma.setCellValueFactory(new PropertyValueFactory<>("plataforma"));

        txtAdministrador.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(
                        Boolean.TRUE.equals(cellData.getValue().getAdministrador()) ? "Sí" : "No"
                )
        );

        txtVersion.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper("v " + version)
        );

        txtFecha.setCellValueFactory(cellData -> {
            Date fecha = cellData.getValue().getFechaCreacion();
            if (fecha == null) {
                return new ReadOnlyStringWrapper("");
            }
            LocalDateTime ldt = fecha.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            return new ReadOnlyStringWrapper(ldt.format(dateTimeFormatter));
        });

        tabla.setItems(contactos);
    }

    private void configurarEventos() {
        btnAñadir.setOnAction(this::agregar);
    }

    private void inicializarCamposFijos() {
        cVersion.setText("v " + version);
        cFecha.setText("");
        setLog("Listo para añadir usuarios.");
    }


    private void configurarClickTabla() {
        tabla.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // doble click
                Usuario seleccionado = tabla.getSelectionModel().getSelectedItem();
                if (seleccionado != null) {
                    mostrarDetalleUsuario(seleccionado);
                }
            }
        });
    }

    private void mostrarDetalleUsuario(Usuario usuario) {
        String admin = Boolean.TRUE.equals(usuario.getAdministrador()) ? "Sí" : "No";
        String fechaStr = "";
        if (usuario.getFechaCreacion() != null) {
            LocalDateTime ldt = usuario.getFechaCreacion().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            fechaStr = ldt.format(dateTimeFormatter);
        }

        String mensaje =
                "Correo: " + usuario.getCorreo() + "\n" +
                        "Plataforma: " + usuario.getPlataforma() + "\n" +
                        "Admin: " + admin + "\n" +
                        "Versión: v " + version + "\n" +
                        "Fecha creación: " + fechaStr;

        javafx.scene.control.Alert alert =
                new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Detalle de usuario");
        alert.setHeaderText("Información del usuario");
        alert.setContentText(mensaje);
        alert.initOwner(tabla.getScene().getWindow());
        alert.showAndWait();
    }
    private void agregar(ActionEvent e) {
        String correo = safeText(cCorreo);
        String plataforma = cPlataforma.getValue() != null ? cPlataforma.getValue().trim() : "";
        boolean esAdmin = cAdministrador.isSelected();

        if (correo.isEmpty() || plataforma.isEmpty()) {
            setLog("Faltan datos.");
            return;
        }
        if (!correo.contains("@")) {
            setLog("Correo inválido.");
            return;
        }
        boolean existe = contactos.stream()
                .anyMatch(c -> c.getCorreo() != null && c.getCorreo().equalsIgnoreCase(correo));
        if (existe) {
            setLog("El usuario con ese correo ya existe.");
            return;
        }
        LocalDateTime ahora = LocalDateTime.now();
        Date fechaDate = Date.from(ahora.atZone(ZoneId.systemDefault()).toInstant());

        Usuario nuevo = new Usuario();
        nuevo.setCorreo(correo);
        nuevo.setPlataforma(plataforma);
        nuevo.setAdministrador(esAdmin);
        nuevo.setVersion(version);
        nuevo.setFechaCreacion(fechaDate);

        contactos.add(nuevo);
        tabla.refresh();
        cFecha.setText(ahora.format(dateTimeFormatter));

        limpiarCampos();
        setLog("Usuario añadido correctamente.");
    }

    private void limpiarCampos() {
        cCorreo.clear();
        cAdministrador.setSelected(false);
        cPlataforma.setValue(PLATAFORMA_POR_DEFECTO);}

    private String safeText(TextField tf) {
        return tf.getText() != null ? tf.getText().trim() : "";
    }

    private void setLog(String mensaje) {
        if (txtLog != null) {
            txtLog.setText(mensaje);
        }
    }
}
