//
//  VideoListView.swift
//  iosApp
//
//  Created by Roberto Garcia Romero on 29/9/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared
import LinkPresentation

struct VideoListView: View {
    
    @EnvironmentObject var navigator: Navigator
    @ObservedObject private var viewModel = VideoListViewModelWrapper()
    @State private var searchText = ""

    var body: some View {
        VStack {
            VideoView(
                isLoading: viewModel.state.isLoading,
                videos: viewModel.state.videos
            )
        }.task {
            viewModel.initialize()
        }
    }

}

struct VideoView: View {
    @State var togglePreview = false

    var isLoading: Bool
    var videos: [VideoBo]
    
    var body: some View {
        if(isLoading) {
            ProgressView()
        } else {
            Text("Videos")
                .font(.title)
                .padding(
                    EdgeInsets(
                        top: 0, leading: 20, bottom: 0, trailing: 0))
            List {
                ForEach(videos, id: \.self) { video in
                    URLPreview(previewURL: URL(string: video.link)!, togglePreview: $togglePreview)
                                    .aspectRatio(contentMode: .fit)
                                    .padding()
                }
            }
        }
    }
}

struct URLPreview : UIViewRepresentable {
    var previewURL:URL
    //Add binding
    @Binding var togglePreview: Bool

    func makeUIView(context: Context) -> LPLinkView {
        let view = LPLinkView(url: previewURL)
        
        let provider = LPMetadataProvider()

        provider.startFetchingMetadata(for: previewURL) { (metadata, error) in
            if let md = metadata {
                DispatchQueue.main.async {
                    view.metadata = md
                    view.sizeToFit()
                    self.togglePreview.toggle()
                }
            }
        }
        
        return view
    }
    
    func updateUIView(_ uiView: LPLinkView, context: UIViewRepresentableContext<URLPreview>) {
    }
}

class VideoListViewModelWrapper: ObservableObject {
    private let viewModel: VideoViewModel = GetViewModels().getVideosViewModel()
    
    @Published var state: VideoState = VideoState(isLoading: false, videos: [])
    
    
    func initialize() {
        viewModel.getVideos()
        FlowWrapper<VideoState>(stateFlow: viewModel.state).observe { states in
            self.state = states ?? VideoState(isLoading: false, videos: [])
        }
    }
    
    
}
#Preview {
    ScheduleListView()
}


#Preview {
    VideoListView()
}
