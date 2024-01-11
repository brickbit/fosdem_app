//
//  PreferencesView.swift
//  iosApp
//
//  Created by Roberto García Romero on 9/1/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct PreferencesView: View {
    @ObservedObject var viewModel: IOSPreferencesViewModel

    @EnvironmentObject var navigator: Navigator

    init() {
        self.viewModel = IOSPreferencesViewModel()
    }
    
    var body: some View {
        VStack(
            alignment: .leading
        ) {
            Text("Tracks")
                .font(.custom("Signika-Bold",size: 48))
            Text("Selecciona los tracks en los que estés más interesado")
                .font(.custom("Signika-Regular",size: 18))
            preferencesContent()
        }
        .padding(.horizontal, 16)
        .padding(.bottom, 16)
        .onAppear {
            viewModel.startObserving()
        }
        .onDisappear {
            viewModel.dispose()
        }
    }
    
    func preferencesContent() -> AnyView {
        switch viewModel.state {
        case .loading: 
            return AnyView(
                ProgressView()
                    .frame(maxWidth: .infinity, maxHeight: .infinity)
            )
        case .loaded(let tracks):
            @State var enabled = tracks.filter({$0.checked}).count > 3

            return AnyView(
                VStack {
                    
                    List(tracks, id: \.self) { track in
                        TrackItemView(
                            track: track,
                            action: { track,checked in viewModel.onTrackChecked(track: track, checked: checked) }
                        )
                    }.listStyle(PlainListStyle())
                    HStack(
                        alignment: .center
                    ) {
                        Button {
                            Task {
                                navigator.navigate(to: .main)
                            }
                        } label: {
                            Text("Continuar")
                                .frame(maxWidth: .infinity)
                        }
                        .disabled(!enabled)
                        .buttonStyle(.borderedProminent)
                        .tint(.purple)
                        .clipShape(Capsule())
                    }
                }
            )
        case .error:
            return AnyView(Text("Error"))
        }
    }
}

struct TrackItemView: View {
    
    @State private var isOn = false
    var track: TrackBo
    var action: (TrackBo, Bool) -> ()
    
    init(
        track: TrackBo,
        action: @escaping (TrackBo, Bool) -> ()
    ) {
        self.track = track
        self.isOn = track.checked
        self.action = action
    }
    
    var body: some View {
        HStack {
            Toggle(isOn: $isOn) {
                Text(track.name)
            }
            .onChange(of: isOn) { value in
                action(self.track, value)
            }
            .toggleStyle(.automatic)
    
        }
    }
}



extension PreferencesView {
    @MainActor class IOSPreferencesViewModel: ObservableObject {
        private let viewModel: PreferencesViewModel
                
        @Published var state: PreferencesStateSwift = PreferencesStateSwift.loading
        
        private var handle: DisposableHandle?

        init() {
            self.viewModel = PreferencesViewModel(getTracks: GetTracksUseCase(repository: ScheduleRepositoryImpl()))
            self.viewModel.getPreferences()
        }
        
        // Observes to state changes
        func startObserving() {
            handle = viewModel.state.subscribe(onCollect: { state in
                if let state = state {
                    self.state = PreferencesStateSwift(state) ?? .loading
                }
            })
        }
        
        func onTrackChecked(
            track: TrackBo,
            checked: Bool
        ) {
            viewModel.onTrackChecked(track: track, checked: checked)
        }
        
        // Removes the listener
        func dispose() {
            handle?.dispose()
            
        }
    }
}


#Preview {
    PreferencesView()
}
