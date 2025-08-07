package br.ufape.cg;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Classe que representa uma malha composta por faces e vértices
public class MalhaFaceVertice {
    private List<Vertice> vertex = new ArrayList<>();
    private List<Face> faces = new ArrayList<>();

    public List<Vertice> getvertex() {
    return this.vertex;
    }

    public List<Face> getFaces() {
        return this.faces;
    }

    public void carregarArquivoOFF(String caminhoArquivo) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo));
        String line;
        // Leitura da primeira linha
        do {
            line = br.readLine();
        } while (line != null && (line.isBlank() || line.startsWith("#")));

        if (!line.equals("OFF")) {
            throw new IOException("Formato inválido: esperado OFF");
        }

        // leitura da linha com quantidades
        line = br.readLine();
        while (line != null && (line.isBlank() || line.startsWith("#"))) {
            line = br.readLine();
        }
        String[] partes = line.trim().split("\\s+");
        int nVertex = Integer.parseInt(partes[0]);
        int nFaces = Integer.parseInt(partes[1]);

        // Ler as vértices
        for (int i = 0; i < nVertex; i++) {
            line = br.readLine();
            while (line != null && (line.isBlank() || line.startsWith("#"))) {
                line = br.readLine();
            }
            String[] coords = line.trim().split("\\s+");
            double x = Double.parseDouble(coords[0]);
            double y = Double.parseDouble(coords[1]);
            double z = Double.parseDouble(coords[2]);
            vertex.add(new Vertice(x, y, z));
        }

        // Ler as faces
        for (int i = 0; i < nFaces; i++) {
            line = br.readLine();
            while (line != null && (line.isBlank() || line.startsWith("#"))) {
                line = br.readLine();
            }
            String[] dados = line.trim().split("\\s+");
            int qtdvertex = Integer.parseInt(dados[0]);
            List<Integer> indices = new ArrayList<>();
            for (int j = 1; j <= qtdvertex; j++) {
                indices.add(Integer.parseInt(dados[j]));
            }
            faces.add(new Face(indices));
        }

        br.close();
    }

    public void imprimirResumo() {
        System.out.println("Vértices: " + vertex.size());
        System.out.println("Faces: " + faces.size());

        for (int i = 0; i < Math.min(5, faces.size()); i++) {
            System.out.println("Face " + i + ": " + faces.get(i).indicesvertex);
        }
    }
}
