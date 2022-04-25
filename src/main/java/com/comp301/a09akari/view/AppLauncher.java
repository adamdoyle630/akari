package com.comp301.a09akari.view;

import com.comp301.a09akari.SamplePuzzles;
import com.comp301.a09akari.controller.Controller;
import com.comp301.a09akari.controller.ControllerImpl;
import com.comp301.a09akari.model.Model;
import com.comp301.a09akari.model.ModelImpl;
import com.comp301.a09akari.model.PuzzleImpl;
import com.comp301.a09akari.model.PuzzleLibrary;
import com.comp301.a09akari.model.PuzzleLibraryImpl;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppLauncher extends Application {

  @Override
  public void start(Stage stage) {

    // Load puzzle library
    PuzzleLibrary puzzles = new PuzzleLibraryImpl();
    puzzles.addPuzzle(new PuzzleImpl(SamplePuzzles.PUZZLE_01));
    puzzles.addPuzzle(new PuzzleImpl(SamplePuzzles.PUZZLE_02));
    puzzles.addPuzzle(new PuzzleImpl(SamplePuzzles.PUZZLE_03));
    puzzles.addPuzzle(new PuzzleImpl(SamplePuzzles.PUZZLE_04));
    puzzles.addPuzzle(new PuzzleImpl(SamplePuzzles.PUZZLE_05));

    // Model
    Model model = new ModelImpl(puzzles);

    // Controller
    Controller controller = new ControllerImpl(model);

    // View
    FXComponent view = new AppView(controller);

    // Make scene
    Scene scene = new Scene(view.render(), 500, 600);
    scene.getStylesheets().add("main.css");
    stage.setScene(scene);

    // Refresh view when model changes
    model.addObserver(
        (Model m) -> {
          scene.setRoot(view.render());
          stage.sizeToScene();
        });

    // Show the stage
    stage.setTitle("Play Akari!");
    stage.show();
  }
}
