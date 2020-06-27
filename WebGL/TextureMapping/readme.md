# HTML Image Texture Mapping & Phong Shading Tutorial

> **This document is tutorial for WebGL, Texture Mapping Using Images & Phong Shading Tutorial.**

## Texture Mapping Using Images

> Example code that loads the texture looks like this

```javascript
function initTextures() {
  cubeTexture = gl.createTexture();
  cubeImage = new Image();
  cubeImage.onload = function() { handleTextureLoaded(cubeImage, cubeTexture); }
  cubeImage.src = "cubetexture.png";
}

function handleTextureLoaded(image, texture) {
  gl.bindTexture(gl.TEXTURE_2D, texture);
  gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, gl.RGBA, gl.UNSIGNED_BYTE, image);
  gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MAG_FILTER, gl.LINEAR);
  gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.LINEAR_MIPMAP_NEAREST);
  gl.generateMipmap(gl.TEXTURE_2D);
  gl.bindTexture(gl.TEXTURE_2D, null);
}
```

- The first thing to do is add code to load the textures. In our case, we'll be using a single texture, mapped onto all six sides of our rotating cube, but the same technique can be used for any number of textures.
- We need to load texture image by creating a WebGL texture object `texture` by calling the WebGL `createTexture()` function.
- Create `Image` object and save loaded image file to `Image` object for using texture.
- To create texture in substance, we need to bind `texture` object to `gl.TEXTURE_2D`. And then, image data will be written to texture.
-  After that we setup filtering and wrapping for the texture based on whether or not the image we download was a power of 2 in both dimensions or not.

### Non-Power of Two Texture Support

> While OpenGL 2.0 and later for the desktop offer full support for **non-power-of-two (NPOT)** textures, **OpenGL ES 2.0 and WebGL have only limited NPOT support.**

- The restrictions are as in the following
  - generateMipmap(target) generates an INVALID_OPERATION error if the level 0 image of the texture currently bound to target has an NPOT width or height.
  - Sampling an NPOT texture in a shader will produce the RGBA color (0, 0, 0, 1) if:
    - The minification filter is set to anything but `NEAREST` or `LINEAR`: in other words, if it uses one of the mipmapped filters.
    - The repeat mode is set to anything but CLAMP_TO_EDGE; repeating NPOT textures are not supported.

> Mipmapping and UV repeating can be disabled with texParameteri(). This will allow non-power-of-two (NPOT) textures at the expense of mipmapping, UV wrapping, UV tiling, and your control over how the device will handle your texture.

```javascript
// gl.NEAREST is also allowed, instead of gl.LINEAR, as neither mipmap.
gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.LINEAR);
// Prevents s-coordinate wrapping (repeating).
gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_S, gl.CLAMP_TO_EDGE);
// Prevents t-coordinate wrapping (repeating).
gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_T, gl.CLAMP_TO_EDGE);
```

- Again, with these parameters, compatible WebGL devices will automatically accept any resolution for that texture (up to their maximum dimensions). **Without performing the above configuration, WebGL requires all samples of NPOT textures to fail by returning transparent black: `rgba(0,0,0,0)`.**

### What is Mipmap?

> How can we minify textures without introducing noise and use all of the texels?
 
- This can be done by **generating a set of optimized textures at different sizes which we can then use at runtime.** Since these textures are pre-generated, they can be filtered using more expensive techniques that use all of the texels, and at runtime OpenGL will select the most appropriate level based on the final size of the texture on the screen.

<br>
<p align = 'center'>
<img width = '850' src = 'https://user-images.githubusercontent.com/39554623/85913430-b5d90200-b86f-11ea-8091-2983ebd48458.png'>
</p>

- The resulting image can have more detail, less noise, and look better overall. Although a bit more memory will be used, rendering can also be faster, as the smaller levels can be more easily kept in the GPU’s texture cache. Let’s take a closer look at the resulting image at 1/8th of its original size, using bilinear filtering without and with mipmaps; the image has been expanded for clarity:

## Phong Reflection Model

> Phong shading may also refer to the specific combination of Phong interpolation and the Phong reflection model, which is an empirical model of local illumination.
 
- It describes the way a surface reflects light as **a combination of the diffuse reflection of rough surfaces with the specular reflection of shiny surfaces.**
- It is based on Bui Tuong Phong's informal observation that shiny surfaces have small intense specular highlights, while dull surfaces have large highlights that fall off more gradually.
- The reflection model also includes an ambient term to account for the small amount of light that is scattered about the entire scene.

<br>
<p align = 'center'>
<img width = '850' src = 'https://user-images.githubusercontent.com/39554623/85913431-b96c8900-b86f-11ea-8120-b02a2e0c410a.png'>
</p>
<br>

## The functions of a tutorial

### Change texture using personal images

<br>
<p align = 'center'>
<img width = '850' src = 'https://user-images.githubusercontent.com/39554623/85913196-9e007e80-b86d-11ea-971b-5e48fee6f9f3.gif'>
</p>
<br>

### Support NPOT image in WebGL by Toggle NPOT button

<br>
<p align = 'center'>
<img width = '850' src = 'https://user-images.githubusercontent.com/39554623/85913195-9b9e2480-b86d-11ea-9e9d-c9a26a4908fe.gif'>
</p>
<br>

### Delete binding texture

<br>
<p align = 'center'>
<img width = '850' src = 'https://user-images.githubusercontent.com/39554623/85913198-9f31ab80-b86d-11ea-9e14-907e086e77e1.gif'>
</p>
<br>

### Control coefficient of Ambient·Diffuse·Specular Reflection and Shininess

<br>
<p align = 'center'>
<img width = '850' src = 'https://user-images.githubusercontent.com/39554623/85913197-9e991500-b86d-11ea-9de1-d7f8e1530770.gif'>
</p>
<br>

### Control the RGB lighting

<br>
<p align = 'center'>
<img width = '850' src = 'https://user-images.githubusercontent.com/39554623/85913199-9fca4200-b86d-11ea-85d7-6ab9c5417f56.gif'>
</p>
<br>

## Source

### Base Code

- Refer to Phong shading source code of Hwanyong Lee<sup> Associate Professor, Software and Computer Engineering</sup>

### Image Source

- All images were taken, edited by Junseop Lim and all rights are reserved.
  - The exception is the Phong shading image taken from Computer Graphics classes of Ajou Univ.

### References

- https://www.learnopengles.com/tag/mipmap
- https://en.wikipedia.org/wiki/Phong_shading
- http://www.cs.toronto.edu/~jacobson/phong-demo/
- https://www.w3schools.com/howto/howto_js_rangeslider.asp  
- https://www.w3schools.com/howto/howto_css_cutout_text.asp
- https://developer.mozilla.org/ko/docs/Web/HTML/Element/Input/file
- https://developer.mozilla.org/ko/docs/Web/API/WebGL_API/Tutorial/Using_textures_in_WebGL
- https://www.khronos.org/webgl/wiki/WebGL_and_OpenGL_Differences#Non-Power_of_Two_Texture_Support

***Junseop Lim, CC BY-NC***