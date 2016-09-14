#version 430
 uniform float incx;
   uniform float incy;
   uniform float scale;

   void main(void)
   {

     if(gl_VertexID == 0) gl_Position = vec4((0.20+incx)*scale, (-0.20+incy)*scale, 0.0, 1.0);
     else if(gl_VertexID == 1) gl_Position = vec4((-0.20+incx)*scale, (-0.20+incy)*scale, 0.0, 1.0);
     else gl_Position = vec4((0.20+incx)*scale, (0.20+incy)*scale, 0.0, 1.0);

   }