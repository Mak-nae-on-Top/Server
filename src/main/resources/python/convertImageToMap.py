#!/usr/bin/python3

import sys

import cv2 as cv
import numpy as np
import PIL.Image as Image
import io
import base64
from PIL import ImageFile

ImageFile.LOAD_TRUNCATED_IMAGES = True


def convert_image_to_map(file_path):
    # Open base64 file and decode it to gray image file
    base64_data = open(file_path, 'r').read()
    b = base64.b64decode(base64_data)
    img = Image.open(io.BytesIO(b))
    im_gray = cv.cvtColor(np.array(img), cv.COLOR_BGR2GRAY)

    thresh, im_bw = cv.threshold(im_gray,128,255,cv.THRESH_BINARY | cv.THRESH_OTSU)
    thresh = 127
    im_bw = cv.threshold(im_gray,thresh,255,cv.THRESH_BINARY)[1]

    # find all your connected components
    nb_components,output,stats,centroids = cv.connectedComponentsWithStats(im_bw,connectivity=8)
    sizes = stats[1:,-1]; nb_components = nb_components - 1
    min_size = 150

    img2 = np.zeros(output.shape)

    for i in range(nb_components):
        if sizes[i] >= min_size:
            img2[output == i + 1] = 255

    kernel = np.ones((5, 5), np.uint8)
    blueprint = cv.dilate(img2, kernel)

    im_uint8 = cv.normalize(blueprint, None, 0, 255, cv.NORM_MINMAX, dtype=cv.CV_8U)

    dst = cv.fastNlMeansDenoising(im_uint8, None, 40.0, 7, 21)
    dst = cv.pyrDown(dst)
    dst = cv.pyrDown(dst)
    dst_base64 = base64.b64encode(dst)
    dst_file = open(file_path, 'w')
    dst_file.write(str(dst_base64))
    dst_file.close()


if __name__ == '__main__':
    # Get image file name through argv
    file_path = sys.argv[1]
    convert_image_to_map(file_path)
