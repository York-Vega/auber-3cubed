// src https://codepen.io/Tibixx/pen/rNaKLZW

var scene, camera, renderer, controls;
nt = 0;
var res = { x: Math.round(window.innerWidth), y: Math.round(document.body.clientHeight * 1) };
config = {
    planeX: 800,
    planeY: 800,
    planeZ: 70,
    xNoiseScale: 200,
    yNoiseScale: 200,
    zNoiseScale: 200,
    noiseSpeed: 0.001,
    cameraSpeed: 1
}

var scene = new THREE.Scene();
var camera = new THREE.PerspectiveCamera(45, res.x / res.y, 0.1, 10000);
camera.position.x = 400;
camera.position.y = 100;
camera.position.z = 0;
camera.lookAt(0, 0, 0);

var renderer = new THREE.WebGLRenderer({ antialias: true });
renderer.setClearColor("#000000");
renderer.setSize(res.x, res.y);
document.getElementById("bacground-parallax").appendChild(renderer.domElement);

var geo = new THREE.PlaneBufferGeometry(config.planeX, config.planeY, 20, 20);
var mat = new THREE.MeshLambertMaterial({ color: 0x000000, side: THREE.DoubleSide });
var plane = new THREE.Mesh(geo, mat);
scene.add(plane);
plane.position.z = 0;
plane.position.x = 0;
plane.position.y = 0;
plane.rotateX(90 * (Math.PI / 180));

var mat = new THREE.ShaderMaterial({
    uniforms: {
        color1: { value: new THREE.Color("#f222ff") },
        color2: { value: new THREE.Color("#8c1eff") }
    },
    vertexShader: document.getElementById("vertSun").innerHTML,
    fragmentShader: document.getElementById("fragPlane").innerHTML,
    wireframe: true
});
var plane2 = new THREE.Mesh(geo, mat);
scene.add(plane2);
plane2.position.z = 0;
plane2.position.x = 0;
plane2.position.y = 0;
plane2.rotateX(90 * (Math.PI / 180));

var geo = new THREE.CircleGeometry(100, 50);
var mat = new THREE.ShaderMaterial({
    uniforms: {
        color1: { value: new THREE.Color("#ff2975") },
        color2: { value: new THREE.Color("#f222ff") }
    },
    vertexShader: document.getElementById("vertSun").innerHTML,
    fragmentShader: document.getElementById("fragSun").innerHTML
});
var sun = new THREE.Mesh(geo, mat);
scene.add(sun);
sun.rotateY(90 * (Math.PI / 180));
sun.position.x = -400;
sun.position.y = 50;
sun.position.z = 0;

var starsGeometry = new THREE.Geometry();
for (var i = 0; i < 3000; i++) {
    var star = new THREE.Vector3();
    star.x = THREE.Math.randFloatSpread(3000);
    star.y = THREE.Math.randFloatSpread(3000);
    star.z = THREE.Math.randFloatSpread(3000);
    starsGeometry.vertices.push(star);
}
var starsMaterial = new THREE.PointsMaterial({ color: 0xFFFFFF });
var starField = new THREE.Points(starsGeometry, starsMaterial);
scene.add(starField);

//ambientLight = new THREE.AmbientLight("#ffffff"), scene.add(ambientLight);
//scene.fog = new THREE.FogExp2(0x666666, 0.005);

function animate() {
    var gpL = plane.geometry.attributes.position;
    nt += config.noiseSpeed;
    for (var i = 0; i < gpL.array.length; i += 3) {
        var zns;
        i <= 816 && i >= 504 ? zns = 0 : zns = config.zNoiseScale;
        gpL.array[i + 2] = -Math.abs(noise.perlin3(gpL.array[i] / config.yNoiseScale, gpL.array[i + 1] / config.xNoiseScale, nt) * (zns));
    }
    gpL.needsUpdate = true;
}

var render = function() {
    requestAnimationFrame(render);
    renderer.render(scene, camera);
    animate();
};
render();