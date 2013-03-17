/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.taman.rtrc.endpoint.data;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import static com.taman.rtrc.endpoint.data.RunnerMessage.fromJSON;

/**
 *
 * @author mohamed_taman
 */
public class RunnerMessageConverter implements Decoder.Text<RunnerMessage>, Encoder.Text<RunnerMessage> {

    @Override
    public RunnerMessage decode(String msg) throws DecodeException {

        return fromJSON(msg);
    }

    @Override
    public boolean willDecode(String msg) {

        return (msg != null && !msg.isEmpty() && msg.split(",").length == 5);

    }

    @Override
    public String encode(RunnerMessage object) throws EncodeException {

        return object.toJSON();
    }
}
