/*
 * Copyright (c) 2009, Sun Microsystems, Inc.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  * Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *  * Neither the name of Sun Microsystems nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  Note:  In order to comply with the binary form redistribution
 *         requirement in the above license, the licensee may include
 *         a URL reference to a copy of the required copyright notice,
 *         the list of conditions and the disclaimer in a human readable
 *         file with the binary form of the code that is subject to the
 *         above license.  For example, such file could be put on a
 *         Blu-ray disc containing the binary form of the code or could
 *         be put in a JAR file that is broadcast via a digital television
 *         broadcast medium.  In any event, you must include in any end
 *         user licenses governing any code that includes the code subject
 *         to the above license (in source and/or binary form) a disclaimer
 *         that is at least as protective of Sun as the disclaimers in the
 *         above license.
 *
 *         A copy of the required copyright notice, the list of conditions and
 *         the disclaimer will be maintained at
 *         https://hdcookbook.dev.java.net/misc/license.html .
 *         Thus, licensees may comply with the binary form redistribution
 *         requirement with a text file that contains the following text:
 *
 *             A copy of the license(s) governing this code is located
 *             at https://hdcookbook.dev.java.net/misc/license.html
 */

/**
 *  A record of a received packet
 **/


import com.hdcookbook.grin.util.Profile;

public class Packet {

    private static String hexDigits = "0123456789abcdef";

    /**
     * Either Profile.TIMER_START, Profile.TIMER_STOP or Profile.MESSAGE
     **/
    public byte type;

    /**
     * Timestamp, in ns
     **/
    public long timestamp;

    /**
     * The unique identifier of this timer run.  Meaningless for MESSAGE.
     **/
    public int id;

    /**
     * null for TIMER_STOP and MESSAGE
     **/
    public MessageKey message;

    /**
     * Meaningless for TIMER_STOP and MESSAGE
     **/
    public byte threadID;

    /**
     * Only valid for MESSAGE
     **/
    public byte[] debugMessage;

    /**
     * Get the debug message.  It's output in the format of the Unix
     * utility "hd", that is, as a hex dump that might take multiple
     * lines.
     **/
    public String getDebugMessage() {
        if (debugMessage == null) {
            return null;
        }
        StringBuffer out = new StringBuffer();
        int ch;
        int count = 0;
        String line = "";
        for (int i = 0; ; i++) {
            if (i < debugMessage.length) {
                ch = (int) 0xff & debugMessage[i];
            } else {
                ch = -1;
            }
            int m = count % 16;
            if (m == 0) {
                if (ch == -1) {
                    break;
                }
                out.append(toHex(count, 8) + ":  ");
            }
            if (m == 8) {
                out.append(" ");
            }
            if (ch == -1) {
                out.append("  ");
            } else {
                out.append(toHex(ch, 2));
                if (ch >= 32 && ch < 127) {
                    line += ((char) ch);
                } else {
                    line += ".";
                }
            }
            if (m == 15)  {
                out.append("   " + line + "\n");
                line = "";
            } else {
                out.append(" ");
            }
            count++;
        }
        return out.toString();
    }

    private static String toHex(int b, int digits) {
        if (digits <= 0) {
            throw new IllegalArgumentException();
        }
        String result = "";
        while (digits > 0 || b > 0) {
            result = hexDigits.charAt(b % 16) + result;
            b = b / 16;
            digits--;
        }
        return result;
    }


}
