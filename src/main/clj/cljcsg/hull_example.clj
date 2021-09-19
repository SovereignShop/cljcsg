(ns cljcsg.hull-example
  (:import
   [java.util ArrayList]
   [eu.mihosoft.vvecmath Vector3d Transform]
   [eu.mihosoft.jcsg PropertyStorage Cube]
   [eu.mihosoft.jcsg.ext.quickhull3d
    Point3d HullUtil]
   [java.nio.file Paths]
   [eu.mihosoft.jcsg FileUtil]))

(ArrayList. [1 2 3])

(Vector3d/UNITY)

(def points
  (ArrayList. [(Point3d. 0.0 0.0 0.0)
               (Point3d. 1.0 0.5 0.0)
               (Point3d. 2.0 0.0 0.0)
               (Point3d. 0.5 0.5 0.5)
               (Point3d. 0.0 0.0 2.0)
               (Point3d. 0.1 0.2 0.3)
               (Point3d. 0.0 2.0 0.0)]))


(def cube (.toCSG (new Cube 2)))
(def two-cubes
  (.union cube (.transformed (.toCSG (Cube. 3)) (.translateX (Transform/unity) 10))))

(def  hull (HullUtil/hull two-cubes (PropertyStorage.)))

(FileUtil/write (Paths/get "sample.stl" (into-array String []))
                (.toStlString hull))


"1. Unhandled java.lang.IllegalAccessError
   failed to access class eu.mihosoft.jcsg.ext.quickhull3d.QuickHull3D from
   class clj.cljcsg.hull_example$eval13435
   (eu.mihosoft.jcsg.ext.quickhull3d.QuickHull3D is in unnamed module of loader
   'app'; clj.cljcsg.hull_example$eval13435 is in unnamed module of loader
   clojure.lang.DynamicClassLoader @4f2cb466)"
