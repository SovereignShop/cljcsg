(ns main.clj.cljcsg.core
  (:import
   [eu.mihosoft.jcsg Cube Sphere CSG Polygon Extrude]
   [eu.mihosoft.vvecmath Transform Vector3d]
   [eu.mihosoft.jcsg.ext.quickhull3d HullUtil ]
   [java.nio.file Paths]
   [eu.mihosoft.jcsg FileUtil]))

;; 2D

(defn polygon
  [points]
  (Polygon/fromPoints points))

(defn square
  ([x y & {:keys [center] :or {center true}}]
   (CSG/fromPolygons
    [(cond-> (Polygon/fromPoints
              [(Vector3d/xyz x 0 0)
               (Vector3d/xyz x y 0)
               (Vector3d/xyz 0 y 0)
               (Vector3d/xyz 0 0 0)])
       center (.translate (Vector3d/xyz (- (/ x 2)) (- (/ y 2)) 0)))])))

(defn arc
  [r a & {:keys [n-vertices] :or {n-vertices 20}}]
  (CSG/fromPolygons
   [(polygon
     (for [x (range n-vertices)]
       (let [d (* x (/ a n-vertices))]
         (Vector3d/xyz (* r (Math/cos d))
                       (* r (Math/sin d))
                       0))))]))

(defn circle
  [r & {:keys [n-vertices] :or {n-vertices 20}}]
  (arc r (* 2 Math/PI) :n-vertices n-vertices))

;; 3D

(defn cube
  ([size]
   (cube size size size))
  ([x y z]
   (Cube. x y z)))

(defn sphere
  ([r]
   (Sphere. r)))

;; Extrusion

(defn extrude
  [[x y z] shape]
  (Extrude/points (Vector3d/xyz x y z)
                  (into []
                        (comp (map #(.-vertices %)) cat (map #(.-pos %)))
                        (.getPolygons shape))))

(defn extrude-z
  [z shape]
  (Extrude/points (Vector3d/xyz 0 0 z)
                  (into []
                        (comp (map #(.-vertices %)) cat (map #(.-pos %)))
                        (.getPolygons shape))))

(defn rotate-extrude
  ([r angle shape]
   (r angle shape 10))
  ([r angle shape n-steps]
   (let [pts (for [x (range n-steps)]
               (let [d (/ angle n-steps)]
                 (Vector3d/xyz (* r (Math/cos d)) 0 (* r (Math/sin d)))))])))

;; Output

(defn ->stl-string [model]
  (.toStlString model))

(comment
  (->> (extrude [0 0 5]
                (square 6 6))
       (->stl-string)
       (spit "sample.stl"))

  )


(comment
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
                  (.toStlString union)))
