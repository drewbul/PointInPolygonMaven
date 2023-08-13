#!/bin/sh
java --module-path $PATH_TO_FX --add-modules=javafx.controls,javafx.fxml,javafx.base,javafx.media -Djavafx.allowjs=true -jar PointInPolygonMaven-1.0.jar wav