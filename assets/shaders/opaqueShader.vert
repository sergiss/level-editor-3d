attribute vec3 a_position;
attribute float a_lighting;
attribute vec2 a_texCoord0;

uniform mat4 u_projTrans;

uniform vec3  u_color;
uniform float u_light;

uniform int u_numPointLights;

struct PointLight
{
	vec3 color;
	vec3 position;
	float radius;
};

uniform PointLight u_pointLights[MAX_POINT_LIGHTS];

varying vec2 v_texCoord;
varying vec4 v_color;

void main() {

	v_texCoord= a_texCoord0;

	gl_Position= u_projTrans * vec4(a_position, 1.0);
	vec3 color = u_color * u_light;
	
	for (int i = 0; i < u_numPointLights; ++i) {
		vec3 lightDir = u_pointLights[i].position - a_position.xyz;
		float dist2   = dot(lightDir, lightDir);
		float value   = u_pointLights[i].radius / (1.0 + dist2);	
		color += u_pointLights[i].color * value * 0.33;
	}
		
	color *= a_lighting;	
		 
	v_color= vec4(color, 1.0);
	    
}