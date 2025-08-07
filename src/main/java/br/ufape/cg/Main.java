package br.ufape.cg;

import java.io.IOException;

import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.FPSAnimator;

public class Main {
    public static void main(String[] args) {
        // Abre uma janela para cada arquivo OFF, permitindo visualizar duas malhas simultaneamente
        abrirMalha("hand-hybrid.off", "Hand Hybrid");
        abrirMalha("triangles.off", "Triangles");
    }

    /**
     * Abre uma janela gráfica para visualizar uma malha 3D a partir de um arquivo OFF.
     * @param nomeArquivo
     * @param tituloJanela
     */
    private static void abrirMalha(String nomeArquivo, String tituloJanela) {
        // Cria o objeto da malha
        MalhaFaceVertice malha = new MalhaFaceVertice();
        try {
            // Localiza o arquivo OFF nos recursos do projeto
            java.net.URL resourceUrl = Main.class.getClassLoader().getResource(nomeArquivo);
            if (resourceUrl == null) {
                throw new IOException("Arquivo '" + nomeArquivo + "' não encontrado.");
            }
            java.io.File file = new java.io.File(resourceUrl.toURI());
            // Carrega os dados da malha a partir do arquivo OFF
            malha.carregarArquivoOFF(file.getAbsolutePath());
            // Imprime um resumo da malha no console
            malha.imprimirResumo();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // Configurações do OpenGL
        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);

        // Cria a janela gráfica NEWT
        GLWindow glWindow = GLWindow.create(capabilities);

        // Adiciona o visualizador da malha à janela
        glWindow.addGLEventListener(new VisualizadorMalha(malha)); 

        glWindow.setSize(800, 600);
        glWindow.setTitle("Visualizar Malha - " + tituloJanela);
        glWindow.setVisible(true);

        // Cria o animador para atualizar a renderização
        final FPSAnimator animator = new FPSAnimator(glWindow, 60, true);

        // Ao fechar a janela, encerra o animador e o programa
        glWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDestroyNotify(WindowEvent e) {
                animator.stop();
                System.exit(0);
            }
        });

        animator.start();
    }
}