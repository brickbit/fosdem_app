//
//  ScheduleView.swift
//  iosApp
//
//  Created by Roberto Garcia Romero on 29/9/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared
import LinkPresentation

struct ScheduleListView: View {
    @EnvironmentObject var navigator: Navigator
    @ObservedObject private var viewModel = ScheduleViewModelWrapper()
    @State private var searchText = ""

    var body: some View {
        VStack {
            ScheduleView(
                isLoading: viewModel.state.isLoading,
                schedules: viewModel.state.schedules,
                rooms: viewModel.state.rooms,
                tracks: viewModel.state.tracks,
                days: viewModel.state.days,
                hours: viewModel.state.hours,
                filterBySpeaker: { title, speaker in
                    viewModel.filterBySpeaker(title: title, speaker: speaker)
                },
                onFilterClicked: { room, track, day, hours in
                    viewModel.filter(room: room, track: track, day: day, hours: hours)
                }
            )
        }.task {
            viewModel.initialize()
        }
    }
}

struct ScheduleView: View {
    @State private var searchText = ""
    @State private var showingFilters = false

    let isLoading: Bool
    let schedules: [ScheduleBo]
    let rooms: [String]
    let tracks: [String]
    let days: [String]
    let hours: [String]
    
    var filterBySpeaker: (String, String) -> Void
    var onFilterClicked: (String, String, String, [String]) -> Void

    @FocusState private var isFocused: Bool

    var body: some View {
        if(isLoading) {
            LoadingView()
        } else {
            VStack(alignment: .leading) {
                Text("Schedules")
                    .font(.title)
                    .padding(
                        EdgeInsets(
                            top: 0, leading: 20, bottom: 0, trailing: 0))
                HStack {
                    Image(systemName: "magnifyingglass").onTapGesture {
                        Task {
                            filterBySpeaker(searchText, "")
                        }
                    }
                    TextField("Search...", text: $searchText)
                        .onChange(of: searchText) { newValue in
                            if(searchText.count > 3) {
                                filterBySpeaker(searchText, "")
                            }
                        }
                        .onSubmit {
                        filterBySpeaker(searchText, "")
                        }.focused($isFocused)
                    Image(systemName: "line.horizontal.3.decrease")
                        .onTapGesture {
                            showingFilters.toggle()
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
                        ScheduleItemView(
                            schedule: schedule
                        )
                    }
                }
                .listStyle(.inset)
                .listRowSeparator(.hidden)
                .sheet(isPresented: $showingFilters) {
                    FilterSheetView(
                        roomList: rooms,
                        trackList: tracks,
                        dayList: days,
                        hours: hours,
                        room: rooms[0],
                        track: tracks[0],
                        day: days[0],
                        onFilterClicked: { room, track, day, hours in
                            showingFilters.toggle()
                            onFilterClicked(room, track, day, hours)
                        }
                    )
                        .presentationDetents([.medium, .large])
                }
            }.onAppear {
                isFocused = true
            }
        }
    }
}



class ScheduleViewModelWrapper: ObservableObject {
    private let viewModel: NewScheduleViewModel = GetViewModels().getSchedulesViewModel()
    
    @Published var state: ScheduleState = ScheduleState(
        isLoading: false,
        schedules: [],
        days: [],
        tracks: [],
        hours: [],
        rooms: [],
        selectedTitle: "",
        selectedDay: "",
        selectedTrack: "",
        selectedHours: [],
        selectedSpeaker: "",
        selectedRoom: ""
    )
    
    
    func initialize() {
        viewModel.getSchedules()
        FlowWrapper<ScheduleState>(stateFlow: viewModel.state).observe { states in
            self.state = states ?? ScheduleState(
                isLoading: false,
                schedules: [],
                days: [],
                tracks: [],
                hours: [],
                rooms: [],
                selectedTitle: "",
                selectedDay: "",
                selectedTrack: "",
                selectedHours: [],
                selectedSpeaker: "",
                selectedRoom: ""
            )
        }
    }
    
    func filter(room: String, track: String, day: String, hours: [String]) {
        viewModel.filter(selectedDay: day, selectedTrack: track, selectedHours: hours, selectedRoom: room)
    }
    
    func filterBySpeaker(title: String, speaker: String) {
        viewModel.filterByTitleOrSpeaker(title: title, speaker: speaker)
    }
    
}
#Preview {
    ScheduleListView()
}
