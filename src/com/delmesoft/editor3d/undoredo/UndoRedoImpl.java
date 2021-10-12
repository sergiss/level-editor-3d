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
package com.delmesoft.editor3d.undoredo;

import com.delmesoft.editor3d.undoredo.changeable.Changeable;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class UndoRedoImpl<T extends Changeable> implements UndoRedo<T> {

	protected Node<T> head;
	protected Node<T> current;

	protected final Pool<Node<T>> pool;

	protected int index;

	protected final int maxChanges;

	protected ChangeListener<T> changeListener;

	public UndoRedoImpl() {
		this(50);
	}

	public UndoRedoImpl(int maxChanges) {
		
		this.maxChanges = maxChanges;
		
		pool = new Pool<UndoRedoImpl<T>.Node<T>>() {
			@Override
			protected UndoRedoImpl<T>.Node<T> newObject() {
				return new Node<T>();
			}
		};

		head = pool.obtain();
		head.prev = head;

		current = head;
	}

	@Override
	public boolean canUndo() {
		return index > 0;
	}

	@Override
	public void undo() {
		if (canUndo()) {

			final Array<T> elements = current.elements;
			
			current = current.prev;
			index--;

			final ChangeListener<T> changeListener = this.changeListener;

			if (changeListener == null) {
				for (int i = 0, n = elements.size; i < n; ++i) {
					elements.get(i).swapStates();
				}
			} else {
				
				T e;
				for (int i = 0, n = elements.size; i < n; ++i) {
					e = elements.get(i);
					e.swapStates();
					changeListener.onElementChange(e);
				}
				changeListener.onChange(this);				
			}
			

		}
	}

	@Override
	public boolean canRedo() {
		return current.next != null;
	}

	@Override
	public void redo() {
		if (canRedo()) {

			final Array<T> elements = current.next.elements;
			current = current.next;
			index++;
			final ChangeListener<T> changeListener = this.changeListener;
			if (changeListener == null) {
				for (int i = 0, n = elements.size; i < n; ++i) {
					elements.get(i).swapStates();
				}
			} else {
				T e;
				for (int i = 0, n = elements.size; i < n; ++i) {
					e = elements.get(i);
					e.swapStates();
					changeListener.onElementChange(e);
				}
				changeListener.onChange(this);				
			}
			
		}
				
	}

	@Override
	public void newChange() {
		if (index > maxChanges) {

			final Node<T> node = head;
			head = node.next;
			head.prev = node.prev;

			// Recycle nodes
			node.clear();
			pool.free(node);

		} else {
			index++;
		}

		// Optimize array
		current.elements.shrink();

		Node<T> node = current.next;

		// Recycle nodes
		while (node != null) {

			Node<T> tmp = node;
			node = node.next;

			tmp.clear();
			pool.free(tmp);

		}

		node = pool.obtain();

		current.next = node;
		node.prev = current;
		head.prev = node;

		current = node;
		if (changeListener != null) {
			changeListener.onChange(this);
		}
	}

	@Override
	public void mem(T e) {
		current.elements.add(e);
	}

	@Override
	public void clear() {
		pool.clear();

		for (Node<T> node = head; node != null; node = node.next) {
			node.clear();
		}

		index = 0;

		// changeListener = null;

		head = pool.obtain();
		head.prev = head;

		current = head;
	}

	@SuppressWarnings("hiding")
	protected class Node<T extends Changeable> {
		Node<T> prev, next;
		Array<T> elements = new Array<T>();
		public void clear() {
			prev = next = null;
			elements.clear();
		}
	}

	@Override
	public void setChangeListener(ChangeListener<T> changeListener) {
		this.changeListener = changeListener;
	}

	@Override
	public ChangeListener<T> getChangeListener() {
		return changeListener;
	}

}
