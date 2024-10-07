//
//  HomeView.swift
//  iosApp
//
//  Created by Roberto Garcia Romero on 29/9/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct HomeView: View {
    @EnvironmentObject var navigator: Navigator
    @ObservedObject private var viewModel = HomeViewModelWrapper()

    var body: some View {
        VStack(alignment: .leading)  {
            HStack{
                Image("logo").resizable().frame(width: 40, height: 40).padding(EdgeInsets(top: 0, leading: 0, bottom: 0, trailing: 8))
                Text("Fosdem 2025").font(.title)
            }.padding(EdgeInsets(top: 0, leading: 0, bottom: 24, trailing: 0))
            VStack {
                HStack {
                    Text("Right now")
                    Spacer()
                }
                if(viewModel.state.rightNowSchedules.isEmpty) {
                    Text("Aquí aparecerán las charlas que hayan empezado o vayan a empezar")
                        .frame(maxWidth: .infinity)
                        .padding()
                        .background(.gray)
                        .cornerRadius(15)
                } else {
                    ScrollView(.horizontal) {
                        LazyHStack {
                            ForEach(viewModel.state.rightNowSchedules, id: \.self) { schedule in
                                ScheduleItemView(
                                    schedule: schedule,
                                    notifyEvent: { schedule in },
                                    notNotifyEvent: { schedule in }
                                ).padding(EdgeInsets(top: 0, leading: 4, bottom: 0, trailing: 4))
                            }
                        }
                    }
                }
            }
            .frame(height: viewModel.state.rightNowSchedules.isEmpty ? 120 : 200)
            .padding(EdgeInsets(top: 0, leading: 0, bottom: 16, trailing: 0))
            VStack {
                HStack {
                    Text("Favourite talks")
                    Spacer()
                    Text("see all").foregroundStyle(Color("AccentColorDark"))
                }
                if(viewModel.state.favouriteSchedules.isEmpty) {
                    Text("Tus charlas favoritas aparecerán aquí. Pulsa en el símbolo del corazón de una charla para guardarla como favorita")
                        .frame(maxWidth: .infinity)
                        .padding()
                        .background(.gray)
                        .cornerRadius(15)
                } else {
                    ScrollView(.horizontal) {
                        LazyHStack {
                            ForEach(viewModel.state.favouriteSchedules, id: \.self) { schedule in
                                ScheduleItemView(
                                    schedule: schedule,
                                    notifyEvent: { schedule in },
                                    notNotifyEvent: { schedule in }
                                ).padding(EdgeInsets(top: 0, leading: 4, bottom: 0, trailing: 4))
                            }
                        }
                    }
                }
            }
            .frame(height: viewModel.state.favouriteSchedules.isEmpty ? 120 : 200)
            .padding(EdgeInsets(top: 0, leading: 0, bottom: 16, trailing: 0))
            HStack {
                Text("Speakers")
                Spacer()
                Text("see all").foregroundStyle(Color("AccentColorDark"))
            }.padding(EdgeInsets(top: 0, leading: 0, bottom: 16, trailing: 0))
            HStack {
                Text("Stands")
                Spacer()
            }
            Spacer()
        }
        .padding()
        .task {
            viewModel.initialize()
        }
    }
}

class HomeViewModelWrapper: ObservableObject {
    private let viewModel: HomeViewModel = GetViewModels().getHomeViewModel()
    
    @Published var state: HomeState = HomeState(
        isLoading: false,
        favouriteSchedules: [],
        rightNowSchedules: [],
        speakers: [],
        stands: []
    )
    
    
    func initialize() {
        viewModel.getFavouriteSchedules()
        viewModel.getRightNowSchedules()
        FlowWrapper<HomeState>(stateFlow: viewModel.state).observe { states in
            self.state = states ?? HomeState(isLoading: false, favouriteSchedules: [], rightNowSchedules: [],
                speakers: [],
                stands: []
            )
        }
    }
}

#Preview {
    HomeView()
}
