/* 
 * Copyright (c) 2021, Sergio S.- sergi.ss4@gmail.com http://sergiosoriano.com
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package com.delmesoft.editor3d.presenter;

import java.io.File;

import com.delmesoft.editor3d.editor.Editor;
import com.delmesoft.editor3d.level.Level;
import com.delmesoft.editor3d.level.Settings;
import com.delmesoft.editor3d.ui.View;
import com.delmesoft.editor3d.ui.panel.BlockPanel;
import com.delmesoft.editor3d.ui.panel.DecalPanel;
import com.delmesoft.editor3d.ui.panel.EntityPanel;
import com.delmesoft.editor3d.ui.panel.EnvironmentPanel;
import com.delmesoft.editor3d.ui.panel.InfoPanel;
import com.delmesoft.editor3d.ui.panel.SettingsPanel;
import com.delmesoft.editor3d.ui.panel.ToolPanel;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;

public interface Presenter {
	
	View getView();

	void createNewProject();

	Editor getEditor();
	
	Camera getCamera();

	BlockPanel getBlockPanel();
	
	SettingsPanel getSettingsPanel();
	
	InfoPanel getInfoPanel();

	EntityPanel getEntityPanel();
	
	DecalPanel getDecalPanel();

	EnvironmentPanel getEnvironmentPanel();
	
	ToolPanel getToolPanel();

	Level getLevel();
	
	void save();

	void saveAs();
	
	void load();
	
	void load(File file);

	void initialize(Settings settings) throws Exception;

	RenderContext getRenderContext();

}
