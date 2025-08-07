package br.ufape.cg;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;

public class VisualizadorMalha implements GLEventListener {

    private MalhaFaceVertice malha;
    private GLU glu = new GLU();

    public VisualizadorMalha(MalhaFaceVertice malha) {
        this.malha = malha;
    }

    // Método principal da lógica de desenho
    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        // Configura a câmera
        // Posição da câmera (olho), para onde ela olha (centro), e qual direção é para cima
        glu.gluLookAt(0.5, 0.5, 2.0,   // Posição do olho 
                      0.5, 0.5, 0.0,   // Ponto para o qual olhamos
                      0.0, 1.0, 0.0);  // Vetor para cima

        // Chama o método que desenha a malha
        desenharMalha(gl);
        gl.glFlush();
    }

    private void desenharMalha(GL2 gl) {
        // Define o modo de desenho para lines (wireframe)
        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_LINE);
        // Itera sobre cada face da malha
        for (Face face : malha.getFaces()) {
            
            // Define o modo de desenho baseado no número de vértices da face
            if (face.indicesvertex.size() == 3) {
                gl.glBegin(GL2.GL_TRIANGLES); // Desenha como triângulos
            } else if (face.indicesvertex.size() == 4) {
                gl.glBegin(GL2.GL_QUADS); // Desenha como quadriláteros
            } else {
                gl.glBegin(GL2.GL_POLYGON); // Modo genérico para outros polígonos
            }

            // Define uma cor para a face (ex: azul)
            gl.glColor3f(0.0f, 0.5f, 1.0f);

            // Itera sobre os índices de vértice de cada face
            for (int indiceVertice : face.indicesvertex) {
                Vertice v = malha.getvertex().get(indiceVertice);
                gl.glVertex3d(v.x, v.y, v.z);
            }

            gl.glEnd();
        }
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        // Define a cor de fundo (preto)
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        // Ativa o teste de profundidade
        gl.glEnable(GL.GL_DEPTH_TEST);
    }
    
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        
        if (height == 0) height = 1;
        float aspect = (float) width / height;

        // Configura a área de visualização
        gl.glViewport(0, 0, width, height);
        
        // Configura a projeção (perspectiva)
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0, aspect, 0.1, 100.0);
        
        // Retorna para a matriz de modelo/visão
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }
}