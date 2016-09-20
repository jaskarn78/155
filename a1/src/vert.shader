#version 430

   uniform float incx;
   uniform float incy;
   uniform float scale;
   uniform float colorFlag;
   out vec4 varyingColor;

   void main(void)
   {

     if(gl_VertexID == 0)
     {
        gl_Position = vec4((0.20+incx)*scale, (-0.20+incy)*scale, 0.0, 1.0);
        varyingColor = vec4(0,0,1,1);
     }else if(gl_VertexID == 1)
     {
      gl_Position = vec4((-0.20+incx)*scale, (-0.20+incy)*scale, 0.0, 1.0);
      varyingColor = vec4(0,1,0,1);
     }else
     {
      gl_Position = vec4((0.20+incx)*scale, (0.20+incy)*scale, 0.0, 1.0);
      varyingColor = vec4(1, 0, 0, 1);
     }
     if(colorFlag == 0.0)
        varyingColor = vec4(0, 0, 1, 1);
   }