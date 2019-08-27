package com.jkspad.tutorial;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * @author John Knight
 * Copyright http://www.jkspad.com
 *
 */
public class ColourPacking extends ApplicationAdapter {
	private Mesh triangleMesh;
	private ShaderProgram shader;

	private final String VERTEX_SHADER =
			"attribute vec4 a_position;\n"
					+ "attribute vec4 a_color;\n"
					+ "varying vec4 v_color;\n"
					+ "void main() {\n"
					+ " gl_Position = a_position;\n"
					+ " v_color = a_color;\n" +
					"}";

	private final String FRAGMENT_SHADER =
			"varying vec4 v_color;\n"
					+ "void main() {\n"
					+ " gl_FragColor = v_color;\n"
					+ "}";


	protected void createMeshShader() {
		ShaderProgram.pedantic = false;
		shader = new ShaderProgram(VERTEX_SHADER, FRAGMENT_SHADER);
		String log = shader.getLog();
		if (!shader.isCompiled()){
			throw new GdxRuntimeException(log);
		}
		if (log!=null && log.length()!=0){
			Gdx.app.log("shader log", log);
		}
	}

	private void createTriangleMesh() {

		triangleMesh = new Mesh(true, 3, 0,
				new VertexAttribute(VertexAttributes.Usage.Position, 2, "a_position"),
				new VertexAttribute(VertexAttributes.Usage.ColorPacked, 4, "a_color")
		);

		float red = Color.toFloatBits(255, 0, 0, 255);
		float white = Color.toFloatBits(255, 255, 255, 255);
		float blue = Color.toFloatBits(0, 0, 255, 255);

		triangleMesh.setVertices(new float[] {
				0f, 0.5f, red,		// red top vertex
				0.5f, -0.5f, white,	// white bottom right vertex
				-0.5f, -0.5f, blue 	// blue bottom left vertex
		});

	}

	@Override
	public void create() {
		createTriangleMesh();
		createMeshShader();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		shader.begin();
		triangleMesh.render(shader, GL20.GL_TRIANGLES, 0, 3);
		shader.end();
	}

	@Override
	public void dispose() {
		super.dispose();
		shader.dispose();
		triangleMesh.dispose();
	}
}
