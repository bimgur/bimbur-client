package ch.fhnw.ima.bimgur.component

object pages {

  // abstracts a page (at least conceptually -> this is a SPA)
  trait Page {
    def tabName: String
    def pageTitle: String
    def hashLink: String
  }

  object Page {

    case object NewAnalysisPage extends Page {
      def tabName = "New"
      def pageTitle = "Start New Analysis"
      def hashLink = "new"
    }

    case object AnalysesPage extends Page {
      def tabName = "Analyses"
      def pageTitle = "Running Analyses"
      def hashLink = "analyses"
    }

    def pages = List[Page](NewAnalysisPage, AnalysesPage)

  }

}
