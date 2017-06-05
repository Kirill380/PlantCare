(ns plant-care-ui.config)

(enable-console-print!)
(goog-define API_PROTOCOL "http")
(goog-define API_HOST "localhost")
(goog-define API_PORT 8083)
(goog-define API_PATH "")

(def api-url
  (str
   API_PROTOCOL "://" API_HOST ":" API_PORT
   (when (seq API_PATH)
    "/" API_PATH)))
