package com.alanwalker.util;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class TexturePackerTool {
	
	public static void main(String[] args) {
		TexturePacker.process(
				"resource/unpacked", 
				"resource/character/alan", 
				"alan");
	}

}
