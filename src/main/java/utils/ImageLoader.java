/*
 * Copyright - Copyright FindingMovie
 * Copyright (C) 2016 Jayamal Kulathunge. All Rights Reserved.
 *
 * Created Date: 9/12/16 7:52 AM
 * Last Modified Date: 9/12/16 7:52 AM
 * File: utils.ImgaeLoader
 *
 * This file is part of FindingMovie.
 *
 * FindingMovie is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FindingMovie is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */

package utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageConsumer;
import java.io.IOException;
import java.net.URL;

/**
 * Created by jayamal on 9/12/16.
 */
public class ImageLoader extends SwingWorker<BufferedImage, BufferedImage> {

    public interface ImageConsumer {
        public void imageLoaded(Image img, String url);
    }

    private ImageConsumer consumer;
    private String url;

    public ImageLoader(ImageConsumer consumer, String url) {
        this.consumer = consumer;
        this.url = url;
    }

    @Override
    protected BufferedImage doInBackground() throws IOException {
        BufferedImage picture = ImageIO.read(new URL(url));
        return picture;

    }

    protected void done() {
        try {
            Image img = get();
            consumer.imageLoaded(img, url);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }


}