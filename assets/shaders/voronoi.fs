// Author: Txoka from ShaderToy.com (Updated by ChatGPT)
// Title: Neutral Dungeon-Inspired Background with Always Using u_color

#ifdef GL_ES
precision highp float;
#endif

#define PI 3.14159265359

uniform vec2 u_resolution;
uniform float u_time;
uniform vec3 u_color;  // Always use this color for lighting and texturing

// Random function for noise generation
float rand(vec2 co) {
    return fract(sin(mod(dot(co.xy, vec2(12.9898, 78.233)), 3.14)) * 43758.5453);
}

// Function for generating stone-like texture with noise, making it feel rough but neutral
float stonePattern(vec2 uv) {
    float n = sin(uv.x * 12.0 + u_time * 0.3) * cos(uv.y * 12.0 + u_time * 0.3);  // Subtle noise for roughness
    return smoothstep(0.4, 0.6, n);  // Natural stone transition
}

// Flickering light effect to simulate torchlight or ambient dungeon lighting
float flickerLight(vec2 uv) {
    return sin(u_time * 0.3 + uv.x * 6.0 + uv.y * 6.0) * 0.2 + 0.5;  // Slow flicker effect
}

// Main shader function
void main() {
    // Normalize pixel coordinates
    vec2 uv = (gl_FragCoord.xy * 2.0 - u_resolution.xy) / u_resolution.y;

    // Create a neutral stone texture based on noise
    float stone = stonePattern(uv);

    // Apply a subtle flickering light effect (simulating torch or ambient light in the dungeon)
    float light = flickerLight(uv);

    // Combine the stone texture with the flickering light, influenced by u_color
    vec3 fragColor = mix(u_color, vec3(1.0), light) * stone;

    // Add some additional randomness for more depth
    fragColor += rand(uv) * 0.1;

    // Ensure that the stone texture and lighting are always influenced by u_color
    fragColor *= u_color;

    // Output the final color with the applied lighting and texture
    gl_FragColor = vec4(fragColor, 1.0);
}
