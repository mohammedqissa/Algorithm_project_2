package sample;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class Controller {

    public Button extract;
    public Button compress;


    String path;
    public TextField a ;
public  void loadData(){



    FileChooser chooser = new FileChooser();
    chooser.getInitialDirectory();
    File file = chooser.showOpenDialog(new Stage());
    path=file.getPath();


    a.setText(file.getPath());

    extract.setDisable(false);
    compress.setDisable(false);



}

    public  void  coFile() throws IOException {
       System.out.println("file path : "+path);
        Compress a = new Compress(path);

    }

    public void deCo() throws IOException {


        Decompress d = new Decompress(path);
    }

}
