(ns cljcsg.extrude-example
  (:import
   [java.util ArrayList]
   [eu.mihosoft.vvecmath Vector3d Transform]
   [eu.mihosoft.jcsg PropertyStorage Cube Extrude Polygon]
   [eu.mihosoft.jcsg.ext.quickhull3d
    Point3d HullUtil]
   [java.nio.file Paths]
   [eu.mihosoft.jcsg FileUtil]))

(def cube (.toCSG (Cube. 3)))

(count (.getPolygons cube))

(def extrusion
  (let [[pts1 pts2]
        (for [z [0 10]]
          (into-array Vector3d
                      [(Vector3d/xyz -5 -5 z)
                       (Vector3d/xyz -5 5 z)
                       (Vector3d/xyz 5 5 z)
                       (Vector3d/xyz 5 -5 z)]))]
    (Extrude/combine (Polygon/fromPoints pts1)
                     (Polygon/fromPoints pts2))))

(FileUtil/write (Paths/get "sample.stl" (into-array String []))
                (.toStlString extrusion))

(def extrusion-2
  (Extrude/points (Vector3d/xyz 0 10 10)
                  (into-array Vector3d
                              [(Vector3d/xyz -5 -5 0)
                               (Vector3d/xyz -5 5 0)
                               (Vector3d/xyz 5 5 0)
                               (Vector3d/xyz 5 -5 0)])))

(FileUtil/write
 (Paths/get "sample.stl" (into-array String []))
 (.toStlString extrusion-2))
