/* Copyright Statement:
 *
 * This software/firmware and related documentation ("MediaTek Software") are
 * protected under relevant copyright laws. The information contained herein
 * is confidential and proprietary to MediaTek Inc. and/or its licensors.
 * Without the prior written permission of MediaTek inc. and/or its licensors,
 * any reproduction, modification, use or disclosure of MediaTek Software,
 * and information contained herein, in whole or in part, shall be strictly prohibited.
 *
 * MediaTek Inc. (C) 2010. All rights reserved.
 *
 * BY OPENING THIS FILE, RECEIVER HEREBY UNEQUIVOCALLY ACKNOWLEDGES AND AGREES
 * THAT THE SOFTWARE/FIRMWARE AND ITS DOCUMENTATIONS ("MEDIATEK SOFTWARE")
 * RECEIVED FROM MEDIATEK AND/OR ITS REPRESENTATIVES ARE PROVIDED TO RECEIVER ON
 * AN "AS-IS" BASIS ONLY. MEDIATEK EXPRESSLY DISCLAIMS ANY AND ALL WARRANTIES,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NONINFRINGEMENT.
 * NEITHER DOES MEDIATEK PROVIDE ANY WARRANTY WHATSOEVER WITH RESPECT TO THE
 * SOFTWARE OF ANY THIRD PARTY WHICH MAY BE USED BY, INCORPORATED IN, OR
 * SUPPLIED WITH THE MEDIATEK SOFTWARE, AND RECEIVER AGREES TO LOOK ONLY TO SUCH
 * THIRD PARTY FOR ANY WARRANTY CLAIM RELATING THERETO. RECEIVER EXPRESSLY ACKNOWLEDGES
 * THAT IT IS RECEIVER'S SOLE RESPONSIBILITY TO OBTAIN FROM ANY THIRD PARTY ALL PROPER LICENSES
 * CONTAINED IN MEDIATEK SOFTWARE. MEDIATEK SHALL ALSO NOT BE RESPONSIBLE FOR ANY MEDIATEK
 * SOFTWARE RELEASES MADE TO RECEIVER'S SPECIFICATION OR TO CONFORM TO A PARTICULAR
 * STANDARD OR OPEN FORUM. RECEIVER'S SOLE AND EXCLUSIVE REMEDY AND MEDIATEK'S ENTIRE AND
 * CUMULATIVE LIABILITY WITH RESPECT TO THE MEDIATEK SOFTWARE RELEASED HEREUNDER WILL BE,
 * AT MEDIATEK'S OPTION, TO REVISE OR REPLACE THE MEDIATEK SOFTWARE AT ISSUE,
 * OR REFUND ANY SOFTWARE LICENSE FEES OR SERVICE CHARGE PAID BY RECEIVER TO
 * MEDIATEK FOR SUCH MEDIATEK SOFTWARE AT ISSUE.
 */

/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.quicksearchbox.ui;

import com.android.quicksearchbox.Corpus;
import com.android.quicksearchbox.SuggestionCursor;
import com.android.quicksearchbox.SuggestionPosition;
import com.android.quicksearchbox.Suggestions;

import android.database.DataSetObserver;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnFocusChangeListener;
import android.widget.BaseAdapter;

/**
 * Uses a {@link Suggestions} object to back a {@link SuggestionsView}.
 */
public class SuggestionsAdapter extends BaseAdapter {

    private static final boolean DBG = false;
    private static final String TAG = "QSB.SuggestionsAdapter";

    private DataSetObserver mDataSetObserver;

    private final SuggestionViewFactory mViewFactory;

    private SuggestionCursor mCursor;

    private Corpus mCorpus = null;

    private Suggestions mSuggestions;

    private SuggestionClickListener mSuggestionClickListener;
    private OnFocusChangeListener mOnFocusChangeListener;

    private boolean mClosed = false;

    public SuggestionsAdapter(SuggestionViewFactory viewFactory) {
        mViewFactory = viewFactory;
    }

    public boolean isClosed() {
        return mClosed;
    }

    public void close() {
        setSuggestions(null);
        mCorpus = null;
        mClosed = true;
    }

    public void setSuggestionClickListener(SuggestionClickListener listener) {
        mSuggestionClickListener = listener;
    }

    public void setOnFocusChangeListener(OnFocusChangeListener l) {
        mOnFocusChangeListener = l;
    }

    public void setSuggestions(Suggestions suggestions) {
        if (mSuggestions == suggestions) {
            return;
        }
        if (mClosed) {
            if (suggestions != null) {
                suggestions.close();
            }
            return;
        }
        if (mDataSetObserver == null) {
            mDataSetObserver = new MySuggestionsObserver();
        }
        // TODO: delay the change if there are no suggestions for the currently visible tab.
        if (mSuggestions != null) {
            mSuggestions.unregisterDataSetObserver(mDataSetObserver);
            mSuggestions.close();
        }
        mSuggestions = suggestions;
        if (mSuggestions != null) {
            mSuggestions.registerDataSetObserver(mDataSetObserver);
        }
        onSuggestionsChanged();
    }

    public Suggestions getSuggestions() {
        return mSuggestions;
    }

    /**
     * Gets the source whose results are displayed.
     */
    public Corpus getCorpus() {
        return mCorpus;
    }

    /**
     * Sets the source whose results are displayed.
     */
    public void setCorpus(Corpus corpus) {
        if (mSuggestions != null) {
            if ((mCorpus == null) && (corpus != null)) {
                // we've just switched from the 'All' corpus to a specific corpus
                // we can filter the existing results immediately.
                if (DBG) Log.d(TAG, "setCorpus(" + corpus.getName() + ") Filter suggestions");
                mSuggestions.filterByCorpus(corpus);
            } else if (corpus != null) {
                // Note, when switching from a specific corpus to 'All' we do not change the
                // suggestions, since they're still relevant for 'All'. Hence 'corpus != null'
                if (DBG) Log.d(TAG, "setCorpus(" + corpus.getName() + ") Clear suggestions");
                mSuggestions.unregisterDataSetObserver(mDataSetObserver);
                mSuggestions.close();
                mSuggestions = null;
            }
        }
        mCorpus = corpus;
        onSuggestionsChanged();
    }

    public int getCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

    public SuggestionPosition getItem(int position) {
        if (mCursor == null) return null;
        return new SuggestionPosition(mCursor, position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return mViewFactory.getSuggestionViewTypeCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (mCursor == null) {
            return 0;
        }
        mCursor.moveTo(position);
        return mViewFactory.getSuggestionViewType(mCursor);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (mCursor == null) {
            throw new IllegalStateException("getView() called with null cursor");
        }
        mCursor.moveTo(position);
        int viewType = mViewFactory.getSuggestionViewType(mCursor);
        SuggestionView view = mViewFactory.getSuggestionView(viewType, convertView, parent);
        view.bindAsSuggestion(mCursor, mSuggestionClickListener);
        View v = (View) view;
        if (mOnFocusChangeListener != null) {
            v.setOnFocusChangeListener(mOnFocusChangeListener);
        }
        return v;
    }

    protected void onSuggestionsChanged() {
        if (DBG) Log.d(TAG, "onSuggestionsChanged(" + mSuggestions + ")");
        SuggestionCursor cursor = getCorpusCursor(mSuggestions, mCorpus);
        changeCursor(cursor);
    }

    /**
     * Gets the cursor containing the currently shown suggestions. The caller should not hold
     * on to or modify the returned cursor.
     */
    public SuggestionCursor getCurrentSuggestions() {
        return mCursor;
    }

    /**
     * Gets the cursor for the given source.
     */
    protected SuggestionCursor getCorpusCursor(Suggestions suggestions, Corpus corpus) {
        if (suggestions == null) return null;
        return suggestions.getPromoted();
    }

    /**
     * Replace the cursor.
     *
     * This does not close the old cursor. Instead, all the cursors are closed in
     * {@link #setSuggestions(Suggestions)}.
     */
    private void changeCursor(SuggestionCursor newCursor) {
        if (DBG) Log.d(TAG, "changeCursor(" + newCursor + ")");
        if (newCursor == mCursor) {
            // Shortcuts may have changed without the cursor changing.
            notifyDataSetChanged();
            return;
        }
        mCursor = newCursor;
        if (mCursor != null) {
            // TODO: Register observers here to watch for
            // changes in the cursor, e.g. shortcut refreshes?
            notifyDataSetChanged();
        } else {
            notifyDataSetInvalidated();
        }
    }

    public void onIcon2Clicked(int position) {
        if (mSuggestionClickListener != null) {
            mSuggestionClickListener.onSuggestionQueryRefineClicked(position);
        }
    }

    private class MySuggestionsObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            onSuggestionsChanged();
        }
    }

}
