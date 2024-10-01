//
//  ScheduleView.swift
//  iosApp
//
//  Created by Roberto Garcia Romero on 29/9/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct ScheduleListView: View {
    @EnvironmentObject var navigator: Navigator
    @ObservedObject private var viewModel = ScheduleViewModelWrapper()
    @State private var searchText = ""

    var body: some View {
        VStack {
            ScheduleView(
                isLoading: viewModel.state.isLoading,
                schedules: viewModel.state.schedules
            )
        }.task {
            viewModel.initialize()
        }
    }
}

struct ScheduleView: View {
    @State private var searchText = ""
    @State private var showingCredits = false

    var isLoading: Bool
    var schedules: [ScheduleBo]
    
    var body: some View {
        if(isLoading) {
            ProgressView()
        } else {
            VStack(alignment: .leading) {
                Text("Schedules")
                    .font(.title)
                    .padding(
                        EdgeInsets(
                            top: 0, leading: 20, bottom: 0, trailing: 0))
                HStack {
                    Image(systemName: "magnifyingglass")
                    TextField("Search...", text: $searchText)
                    Image(systemName: "line.horizontal.3.decrease")
                        .onTapGesture {
                            showingCredits.toggle()

                        }
                }
                    .padding(12)
                    .overlay(
                        RoundedRectangle(cornerRadius: 25)
                            .stroke(Color.black, lineWidth: 2)
                    )
                    .padding()
                List {
                    ForEach(schedules, id: \.self) { schedule in
                        ScheduleItem(
                            schedule: schedule
                        )
                    }
                }
                .listStyle(.inset)
                .listRowSeparator(.hidden)
                .sheet(isPresented: $showingCredits) {
                    Text("TMore filters")
                        .presentationDetents([.medium, .large])
                }
            }
        }
    }
}

struct ScheduleItem: View {
    var schedule: ScheduleBo
    
    var body: some View {
        HStack(alignment: .top) {
            VStack {
                Text("03").foregroundStyle(.white)
                Text(schedule.start).foregroundStyle(.white)
            }
            .frame(height: 135)
            .padding(EdgeInsets(top: 0, leading: 4, bottom: 0, trailing: 4))
            .background(
                Color.black,
                in: RoundedRectangle(
                    cornerRadius: 20,
                    style: .continuous
                )
            )
            VStack(alignment: .leading) {
                Text(schedule.title).padding(EdgeInsets(top: 0, leading: 0, bottom: 16, trailing: 0))
                ForEach(schedule.speaker, id: \.self) { speaker in
                    Text(speaker)
                        .font(.system(size: 12))
                }

            }.padding(EdgeInsets(top: 8, leading: 8, bottom: 8, trailing: 8))
            Spacer()
        }
        .frame(height: 135)
        .cornerRadius(20)
        .overlay(
            RoundedRectangle(cornerRadius: 20)
                .stroke(.black, lineWidth: 2)
        )
    }
}

class ScheduleViewModelWrapper: ObservableObject {
    private let viewModel: NewScheduleViewModel = GetViewModels().getSchedulesViewModel()
    
    @Published var state: ScheduleState = ScheduleState(isLoading: false, schedules: [])
    
    
    func initialize() {
        viewModel.getSchedules(
            date: "",
            start: "",
            duration: "",
            title: "",
            track: "",
            type: "",
            speaker: ""
        )
        FlowWrapper<ScheduleState>(stateFlow: viewModel.state).observe { states in
            self.state = states ?? ScheduleState(isLoading: false, schedules: [])
        }
    }
    
    
}
#Preview {
    ScheduleListView()
}
