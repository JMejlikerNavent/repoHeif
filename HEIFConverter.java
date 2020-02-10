import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;

public class HEIFConverter {
    static {
        System.loadLibrary("heif"); // hello.dll (Windows) or libhello.so (Unixes)
    }

    private native int convert(int byteDataLength, byte[] byteData ,String outputFileName);

    public byte[] convertir(byte[] byteData) throws Exception{
        try {
            File fileOutput = File.createTempFile("output", ".jpg");
            fileOutput.deleteOnExit();

            int respuesta = convert(byteData.length,byteData,fileOutput.getPath()); // Allocate an instance and invoke the native.
            if (respuesta == 2) {
                System.out.println("No se puede convertir contenedor con 2 o mas imagenes");
                throw new Exception("No se puede convertir contenedor con 2 o mas imagenes");
            }
            
            byte[] byteDataOutput = Files.readAllBytes(fileOutput.toPath());
            return byteDataOutput;

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    public static void main(String[] args) {
        File fileInput = new File(args[0]);
        try {
            new HEIFConverter().convertir(Files.readAllBytes(fileInput.toPath()));
        } catch (Exception e) {
            System.err.println("Cagamos");
        }
    }
}