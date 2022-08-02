package main.view;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Function;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.Node;
import main.util.Pair;

public final class GUIFactoryImpl implements GUIFactory {

    private final Pair<Double, Double> buttonSize;
    private final Insets panelInsets;
    private final double panelSpacing;
    private final Pair<Double, Double> sceneSize;
    private final double length;

    private GUIFactoryImpl(final Pair<Double, Double> buttonSize, final Insets panelInsets, final double panelSpacing,
            final Pair<Double, Double> sceneSize) {
        super();
        this.buttonSize = buttonSize;
        this.panelInsets = panelInsets;
        this.panelSpacing = panelSpacing;
        this.sceneSize = sceneSize;
        length =  Math.sqrt(sceneSize.getX() * sceneSize.getX() + sceneSize.getY() * sceneSize.getY());
    }

    @Override
    public Button createButton(final String name) {
        final Button b = new Button(name);
        b.setPrefSize(buttonSize.getX(), buttonSize.getY());
        return b;
    }

    @Override
    public HBox createHorizontalPanel() {
        final HBox hbox = new HBox();
        hbox.setPadding(panelInsets);
        hbox.setSpacing(panelSpacing);
        return hbox;
    }

    @Override
    public VBox createVerticalPanel() {
        final VBox vbox = new VBox();
        vbox.setPadding(panelInsets);
        vbox.setSpacing(panelSpacing);
        return vbox;
    }

    @Override
    public Alert createInformationBox(final String message) {
        final Alert a = new Alert(AlertType.INFORMATION);
        a.setTitle("Information box");
        a.setHeaderText("A lovely tip!");
        a.setContentText(message);
        return a;
    }

    @Override
    public Scene createScene(final Pane panel) {
        return new Scene(panel, sceneSize.getX(), sceneSize.getY());
    }

    public static class Builder {
        // these things should be read from configuration file in xml.
        // i need to settle it better later.
        private static final double BUTTONWIDTH = 0.15;
        private static final double BUTTONHEIGHT = 0.05;
        private static final double SCENEWIDTH = 0.681;
        private static final double SCENEHEIGHT = 0.681;
        private static final double INSETSX = 0.015;
        private static final double INSETSY = 0.015;
        private static final double PANELSPACING = 0.001;

        private Pair<Double, Double> buttonSize;
        private Pair<Double, Double> sceneSize;
        private Insets panelInsets;
        private double panelSpacing;
        private final double x;
        private final double y;

        private final Function<Double, Double> fx;
        private final Function<Double, Double> fy;

        public Builder(final double x, final double y) {
            super();
            fx = p -> x * p;
            fy = p -> y * p;
            this.buttonSize = new Pair<>(fx.apply(BUTTONWIDTH), fy.apply(BUTTONHEIGHT));
            this.sceneSize = new Pair<>(fx.apply(SCENEWIDTH), fy.apply(SCENEHEIGHT));
            this.panelInsets = new Insets(fx.apply(INSETSX), fy.apply(INSETSY), fx.apply(INSETSX), fy.apply(INSETSY));
            final double h = Math.sqrt(x * x + y * y);
            this.panelSpacing = h * PANELSPACING;
            this.x = x;
            this.y = y;
        }

        /**
         * Set size for a normal button.
         * 
         * @param pX percentage to multiply the screen size.
         * @param pY percentage to multiply the screen size.
         * @return the the object itself.
         */
        public Builder buttonSize(final double pX, final double pY) {
            this.buttonSize = new Pair<>(fx.apply(pX), fy.apply(pY));
            return this;
        }

        /**
         * Set size for a normal scene.
         * 
         * @param p percentage to multiply the screen size
         * @return the object itself
         */
        public Builder sceneSize(final double p) {
            this.sceneSize = new Pair<>(fx.apply(p), fy.apply(p));
            return this;
        }

        /**
         * set size for insets.
         * 
         * @param p percentage to multiply the screen size.
         * @return the object itself
         */
        public Builder panelInsets(final double p) {
            this.panelInsets = new Insets(fx.apply(p), fy.apply(p), fx.apply(p), fy.apply(p));
            return this;
        }

        /**
         * set panel spacing size.
         * 
         * @param p percentage to multiply the screen size.
         * @return the object itself
         */
        public Builder panelSpacing(final double p) {
            final double h = Math.sqrt(x * x + y * y);
            this.panelSpacing = h * p;
            return this;
        }

        public final GUIFactoryImpl build() {
            return new GUIFactoryImpl(buttonSize, panelInsets, panelSpacing, sceneSize);
        }

    }

    @Override
    public ScrollPane createBlockScheda(final Node title, final Collection<? extends Node> descriptions,
            final Collection<? extends Node>... fields) {
        final ScrollPane scroll = new ScrollPane();
        scroll.setPadding(new Insets(panelSpacing * 10));
        final VBox vbox = createVerticalPanel();
        final HBox titleBox = createHorizontalPanel();
        titleBox.getChildren().add(title);
        final HBox descriptionBox = createHorizontalPanel();
        descriptionBox.getChildren().addAll(descriptions);
        final HBox dataBox = createHorizontalPanel();

        final List<Collection<? extends Node>> dataFields = List.of(fields);
        for (final Collection<? extends Node> field : dataFields) {
            final VBox v = createVerticalPanel();
            v.getChildren().addAll(field);
            dataBox.getChildren().add(v);
        }

        vbox.getChildren().addAll(titleBox, descriptionBox, dataBox);
        scroll.setContent(vbox);
        return scroll;
    }

    @Override
    public <T> List<Text> transformStringIntoText(final List<T> list, final int size) {
        return list.stream().map(x -> createText(x, size)).collect(Collectors.toList());
    }

    @Override
    public <T> Text createText(final T s, final int size) {
        String str = "";
        if (s instanceof Number) {
            str = new DecimalFormat("#.##").format(s);
        } else if (s instanceof String) {
            str = s.toString();
        }
        final Text t = new Text(str);
        t.setFont(Font.font("Verdana", FontWeight.NORMAL, size + 0.005 * length));
        return t;
    }

}
