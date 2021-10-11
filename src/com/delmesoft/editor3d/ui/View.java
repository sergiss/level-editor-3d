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
package com.delmesoft.editor3d.ui;

import java.awt.Canvas;
import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.delmesoft.editor3d.level.Settings;
import com.delmesoft.editor3d.presenter.Presenter;

public interface View {
	
	void setPresenter(Presenter presenter);
	
	Presenter getPresenter();

	void setVisible(boolean visible);
	
	void addToolPanel(String name, JPanel panel);
	
	void removeToolPanel(String name);

	void setSettingsPanel(JPanel panel);

	void setInfoPanel(JPanel panel);

	void setCanvas(Canvas canvas);

	void addToolbarSeparator();

	void addToolbarComponent(Component component);

	Settings openNewProjectDialog();

	void showMessageError(String title, String message);
	
	void showMessageInfo(String title, String message);
	
	boolean showQuestion(String title, String message);

	JFrame getJFrame();

	void setSaveButtonEnabled(boolean enabled);
	
	int getTabbePaneToolSelectedIndex();

	void setTitle(String title);

}
