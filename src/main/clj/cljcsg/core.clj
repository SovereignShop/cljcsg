(ns main.clj.cljcsg.core
  (:import
   [eu.mihosoft.jcsg Cube Sphere CSG]
   [eu.mihosoft.vvecmath Transform]
   [java.nio.file Paths]
   [eu.mihosoft.jcsg FileUtil]))

(def cube (.toCSG (new Cube 2)))
(def sphere (.toCSG (Sphere. 1.25 10 10)))

(def cube-plus-sphere (.union cube sphere))
(def cube-minus-sphere (.difference cube sphere))
(def cube-intersect-sphere (.intersect cube sphere))

(def union
  (-> cube
      (.union (.transformed sphere (.translateX (Transform/unity) 3)))
      (.union (.transformed cube-plus-sphere (.translateX (Transform/unity) 6)))
      (.union (.transformed cube-minus-sphere (.translateX (Transform/unity) 9)))
      (.union (.transformed cube-intersect-sphere (.translateX (Transform/unity) 12)))))

(FileUtil/write (Paths/get "sample.stl" (into-array String []))
                (.toStlString union))
