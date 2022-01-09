/*
 *  MusicTag Copyright (C)2003,2004
 *
 *  This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser
 *  General Public  License as published by the Free Software Foundation; either version 2.1 of the License,
 *  or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 *  the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License along with this library; if not,
 *  you can get a copy from http://www.opensource.org/licenses/lgpl-license.php or write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package ealvatag.tag.id3.framebody;

import java.nio.ByteBuffer;

import ealvatag.tag.InvalidTagException;
import ealvatag.tag.datatype.DataTypes;
import ealvatag.tag.datatype.NumberHashMap;
import ealvatag.tag.datatype.PartOfSet;
import ealvatag.tag.id3.valuepair.TextEncoding;
import okio.Buffer;

/**
 * Track number/position in set Text Information frame.
 * <p>
 * <p>The 'Track number/Position in set' frame is a numeric string containing the order number of the audio-file on its original recording.
 * <p>
 * This may be extended with a "/" character and a numeric string containing the total number of tracks/elements on the original recording.
 * e.g. "4/9".
 * <p>
 * Some applications like to prepend the track number with a zero to aid sorting, (i.e 02 comes before 10)
 * <p>
 * <p>
 * <p>For more details, please refer to the ID3 specifications:
 * <ul>
 * <li><a href="http://www.id3.org/id3v2.3.0.txt">ID3 v2.3.0 Spec</a>
 * </ul>
 *
 * @author : Paul Taylor
 * @author : Eric Farng
 * @version $Id$
 */
public abstract class AbstractFrameBodyNumberTotal extends AbstractID3v2FrameBody {
    /**
     * Creates a new FrameBodyTRCK datatype.
     */
    public AbstractFrameBodyNumberTotal() {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, TextEncoding.ISO_8859_1);
        setObjectValue(DataTypes.OBJ_TEXT, new PartOfSet.PartOfSetValue());
    }

    public AbstractFrameBodyNumberTotal(AbstractFrameBodyNumberTotal body) {
        super(body);
    }

    /**
     * Creates a new FrameBodyTRCK datatype, the value is parsed literally
     *
     * @param textEncoding
     * @param text
     */
    public AbstractFrameBodyNumberTotal(byte textEncoding, String text) {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, textEncoding);
        setObjectValue(DataTypes.OBJ_TEXT, new PartOfSet.PartOfSetValue(text));
    }

    public AbstractFrameBodyNumberTotal(byte textEncoding, Integer trackNo, Integer trackTotal) {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, textEncoding);
        setObjectValue(DataTypes.OBJ_TEXT, new PartOfSet.PartOfSetValue(trackNo, trackTotal));
    }

    /**
     * Creates a new FrameBodyTRCK datatype.
     *
     * @param byteBuffer
     * @param frameSize
     * @throws java.io.IOException
     * @throws ealvatag.tag.InvalidTagException
     */
    public AbstractFrameBodyNumberTotal(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException {
        super(byteBuffer, frameSize);
    }

    public AbstractFrameBodyNumberTotal(Buffer byteBuffer, int frameSize) throws InvalidTagException {
        super(byteBuffer, frameSize);
    }

    public String getUserFriendlyValue() {
        PartOfSet.PartOfSetValue value = (PartOfSet.PartOfSetValue) getObjectValue(DataTypes.OBJ_TEXT);
        return String.valueOf(value.getCount());
    }

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    public abstract String getIdentifier();

    public String getText() {
        return getObjectValue(DataTypes.OBJ_TEXT).toString();
    }

    public void setText(String text) {
        setObjectValue(DataTypes.OBJ_TEXT, new PartOfSet.PartOfSetValue(text));
    }

    public Integer getNumber() {
        PartOfSet.PartOfSetValue value = (PartOfSet.PartOfSetValue) getObjectValue(DataTypes.OBJ_TEXT);
        return value.getCount();
    }

    public void setNumber(Integer trackNo) {
        ((PartOfSet.PartOfSetValue) getObjectValue(DataTypes.OBJ_TEXT)).setCount(trackNo);
    }

    public void setNumber(String trackNo) {
        ((PartOfSet.PartOfSetValue) getObjectValue(DataTypes.OBJ_TEXT)).setCount(trackNo);
    }

    public String getNumberAsText() {
        return ((PartOfSet.PartOfSetValue) getObjectValue(DataTypes.OBJ_TEXT)).getCountAsText();
    }

    public Integer getTotal() {
        return ((PartOfSet.PartOfSetValue) getObjectValue(DataTypes.OBJ_TEXT)).getTotal();
    }

    public void setTotal(Integer trackTotal) {
        ((PartOfSet.PartOfSetValue) getObjectValue(DataTypes.OBJ_TEXT)).setTotal(trackTotal);
    }

    public void setTotal(String trackTotal) {
        ((PartOfSet.PartOfSetValue) getObjectValue(DataTypes.OBJ_TEXT)).setTotal(trackTotal);
    }

    public String getTotalAsText() {
        return ((PartOfSet.PartOfSetValue) getObjectValue(DataTypes.OBJ_TEXT)).getTotalAsText();
    }

    protected void setupObjectList() {
        addDataType(new NumberHashMap(DataTypes.OBJ_TEXT_ENCODING, this, TextEncoding.TEXT_ENCODING_FIELD_SIZE));
        addDataType(new PartOfSet(DataTypes.OBJ_TEXT, this));
    }
}
