/**
 * Created by Jaskarn Jagpal on 9/6/2016.
 */
import java.awt.*;
import java.awt.event.*;
import java.nio.*;
import javax.swing.*;
import static com.jogamp.opengl.GL4.*;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.util.FPSAnimator;
import graphicslib3D.GLSLUtils;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

import com.jogamp.opengl.GLAutoDrawable;

public class a1 extends JFrame implements GLEventListener, MouseWheelListener {
    private GLCanvas myCanvas;
    private int rendering_program;
    private int vao[] = new int[1];
    private GLSLUtils util = new GLSLUtils();
    private Button button1, button2, button3;
    private boolean flag = false;

    private float x = 0.0f;
    private float y = 0.0f;
    private float scale = 1.0f;
    private float incx = 0.01f;
    private float incy = 0.00f;
    private float angle = 0.01f;
    private double radius = 0.75f;
    private FPSAnimator animator;


    public a1()
    {	setTitle("CSc 155 Assignment 1");
        setSize(600, 400);
        myCanvas = new GLCanvas();
        myCanvas.addGLEventListener(this);
        myCanvas.addMouseWheelListener(this);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(myCanvas, BorderLayout.CENTER);

        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));

        button1 = new Button("UP/Down");
        button2 = new Button("Circle");
        button3 = new Button("Color");

        sidePanel.add(button1);
        sidePanel.add(button2);
        sidePanel.add(button3);

        getContentPane().add(sidePanel, BorderLayout.WEST);
        setVisible(true);
        animator = new FPSAnimator(myCanvas, 30);
        animator.start();
    }

    public void display(GLAutoDrawable drawable)
    {	GL4 gl = (GL4) GLContext.getCurrentGL();
        gl.glUseProgram(rendering_program);

        float bkg[] = {0.0f, 0.0f, 0.0f, 1.0f};
        FloatBuffer bkgBuffer = Buffers.newDirectFloatBuffer(bkg);
        gl.glClearBufferfv(GL_COLOR, 0, bkgBuffer);
        checkFlag();

        angle+=0.01f;

        if(x > 0.5f) incx = -0.01f;
        if(x < -0.5) incx = 0.01f;

        if(y > 0.5f) incy = -0.01f;
        if(y< -0.5f) incy = 0.01f;

        upDown();
        circle();


        int offset_loc = gl.glGetUniformLocation(rendering_program, "incx");
        int offset_y = gl.glGetUniformLocation(rendering_program, "incy");
        int scale_pts= gl.glGetUniformLocation(rendering_program, "scale");

        gl.glProgramUniform1f(rendering_program, scale_pts, scale);
        gl.glProgramUniform1f(rendering_program, offset_loc, x);
        gl.glProgramUniform1f(rendering_program, offset_y, y);
        gl.glDrawArrays(GL_TRIANGLES,0,3);


    }

    private void upDown()
    {
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                flag=false;
                incx = 0.0f;
                x=0.0f;
                incy=0.01f;
            }
        });
    }


    private void circle()
    {
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                flag=true;
                if(angle>360.0)
                    angle = 0.0f;
            }
        });
    }

    private void checkFlag()
    {
        if(flag == true)
        {
            x=(float)((Math.cos(angle)*radius)+incx);
            y=(float)((Math.sin(angle)*radius)+incy);
        }else{
            x=(x+incx);
            y=(y+incy);
        }
    }

    public void init(GLAutoDrawable drawable)
    {	GL4 gl = (GL4) GLContext.getCurrentGL();
        rendering_program = createShaderProgram();
        gl.glGenVertexArrays(vao.length, vao, 0);
        gl.glBindVertexArray(vao[0]);
    }


    private int createShaderProgram()
    {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        int[] vertCompiled = new int[1];
        int[] fragCompiled = new int[1];
        int[] linked = new int[1];

        String vshaderSource[] = readShaderSource("src/vert.shader");
        String fshaderSource[] = readShaderSource("src/frag.shader");

        int vShader = gl.glCreateShader(GL_VERTEX_SHADER);
        gl.glShaderSource(vShader, vshaderSource.length, vshaderSource, null, 0);
        gl.glCompileShader(vShader);

        util.checkOpenGLError();
        gl.glGetShaderiv(vShader, GL_COMPILE_STATUS, vertCompiled, 0);
        if(vertCompiled[0] == 1)
        {
            System.out.println("vertex compilation success");
        }else{
            System.out.println("vertex compilation failed");
            util.printShaderLog(vShader);
        }
        int fShader = gl.glCreateShader(GL_FRAGMENT_SHADER);
        gl.glShaderSource(fShader, fshaderSource.length, fshaderSource, null, 0);
        gl.glCompileShader(fShader);

        util.checkOpenGLError();
        gl.glGetShaderiv(fShader, GL_COMPILE_STATUS, fragCompiled, 0);
        if(fragCompiled[0] == 1)
        {
            System.out.println("fragment compilation success");
        }else{
            System.out.println("fragment compilation failed");
            util.printShaderLog(fShader);
        }

        int vfprogram = gl.glCreateProgram();
        gl.glAttachShader(vfprogram, vShader);
        gl.glAttachShader(vfprogram, fShader);
        gl.glLinkProgram(vfprogram);

        util.checkOpenGLError();
        gl.glGetProgramiv(vfprogram, GL_LINK_STATUS, linked, 0);
        if(linked[0] == 1)
        {
            System.out.println("linking succeeded");
        }else{
            System.out.println("linking failed");
            util.printProgramLog(vfprogram);
        }
        gl.glDeleteShader(vShader);
        gl.glDeleteShader(fShader);
        return vfprogram;
    }

    public static void main(String[] args) { new a1(); }
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

    }
    public void dispose(GLAutoDrawable drawable) {}

    private String[] readShaderSource(String filename)
    {
        Vector<String> lines = new Vector<String>();
        Scanner sc;
        try
        {
            sc = new Scanner(new File(filename));
        }catch (IOException e)
        {
            System.err.println("IOException reading file: " + e);
            return null;
        }
        while(sc.hasNext())
        {
            lines.addElement(sc.nextLine());
        }
        String[] program = new String[lines.size()];
        for(int i=0; i<lines.size(); i++)
        {
            program[i] = (String) lines.elementAt(i)+ "\n";
        }
        return program;
    }


    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(e.getPreciseWheelRotation() > 0)
            scale = (float) (scale+(Math.abs(e.getPreciseWheelRotation())));
        else {
            scale = (float) (scale+Math.abs(e.getPreciseWheelRotation()));
            scale = scale/2;
        }
    }
}

