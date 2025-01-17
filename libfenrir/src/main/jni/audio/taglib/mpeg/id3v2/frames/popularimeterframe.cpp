/***************************************************************************
    copyright            : (C) 2008 by Lukas Lalinsky
    email                : lalinsky@gmail.com
 ***************************************************************************/

/***************************************************************************
 *   This library is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU Lesser General Public License version   *
 *   2.1 as published by the Free Software Foundation.                     *
 *                                                                         *
 *   This library is distributed in the hope that it will be useful, but   *
 *   WITHOUT ANY WARRANTY; without even the implied warranty of            *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU     *
 *   Lesser General Public License for more details.                       *
 *                                                                         *
 *   You should have received a copy of the GNU Lesser General Public      *
 *   License along with this library; if not, write to the Free Software   *
 *   Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA         *
 *   02110-1301  USA                                                       *
 *                                                                         *
 *   Alternatively, this file is available under the Mozilla Public        *
 *   License Version 1.1.  You may obtain a copy of the License at         *
 *   http://www.mozilla.org/MPL/                                           *
 ***************************************************************************/

#include "popularimeterframe.h"

using namespace TagLib;
using namespace ID3v2;

class PopularimeterFrame::PopularimeterFramePrivate
{
public:
  String email;
  int rating { 0 };
  unsigned int counter { 0 };
};

////////////////////////////////////////////////////////////////////////////////
// public members
////////////////////////////////////////////////////////////////////////////////

PopularimeterFrame::PopularimeterFrame() :
  Frame("POPM"),
  d(std::make_unique<PopularimeterFramePrivate>())
{
}

PopularimeterFrame::PopularimeterFrame(const ByteVector &data) :
  Frame(data),
  d(std::make_unique<PopularimeterFramePrivate>())
{
  setData(data);
}

PopularimeterFrame::~PopularimeterFrame() = default;

String PopularimeterFrame::toString() const
{
  return d->email + " rating=" + String::number(d->rating) + " counter=" + String::number(d->counter);
}

String PopularimeterFrame::email() const
{
  return d->email;
}

void PopularimeterFrame::setEmail(const String &s)
{
  d->email = s;
}

int PopularimeterFrame::rating() const
{
  return d->rating;
}

void PopularimeterFrame::setRating(int s)
{
  d->rating = s;
}

unsigned int PopularimeterFrame::counter() const
{
  return d->counter;
}

void PopularimeterFrame::setCounter(unsigned int s)
{
  d->counter = s;
}

////////////////////////////////////////////////////////////////////////////////
// protected members
////////////////////////////////////////////////////////////////////////////////

void PopularimeterFrame::parseFields(const ByteVector &data)
{
  int pos = 0, size = static_cast<int>(data.size());

  d->email = readStringField(data, String::Latin1, &pos);

  d->rating = 0;
  d->counter = 0;
  if(pos < size) {
    d->rating = static_cast<unsigned char>(data[pos++]);
    if(pos < size) {
      d->counter = data.toUInt(static_cast<unsigned int>(pos));
    }
  }
}

ByteVector PopularimeterFrame::renderFields() const
{
  ByteVector data;

  data.append(d->email.data(String::Latin1));
  data.append(textDelimiter(String::Latin1));
  data.append(static_cast<char>(d->rating));
  data.append(ByteVector::fromUInt(d->counter));

  return data;
}

////////////////////////////////////////////////////////////////////////////////
// private members
////////////////////////////////////////////////////////////////////////////////

PopularimeterFrame::PopularimeterFrame(const ByteVector &data, Header *h) :
  Frame(h),
  d(std::make_unique<PopularimeterFramePrivate>())
{
  parseFields(fieldData(data));
}
