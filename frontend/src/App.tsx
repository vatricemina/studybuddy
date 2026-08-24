import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from './assets/vite.svg'
import heroImg from './assets/hero.png'
import './App.css'
import {Routes, Route, Navigate} from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import DashboardPage from './pages/DashboardPage';
import RegisterPage from './pages/RegisterPage';
import ProtectedRoute from './components/ProtectedRoute';
import TopicsPage from './pages/TopicsPage';
import FlashcardsPage from './pages/FlashcardsPage';
import QuizPage from './pages/QuizPage';
import QuizHistoryPage from './pages/QuizHistoryPage';
import QuizDetailPage from './pages/QuizDetailPage';



function App() {
  return (
      <Routes>
          <Route path="/" element={<Navigate to="/login" />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route
              path="/dashboard"
              element={
                  <ProtectedRoute>
                      <DashboardPage />
                  </ProtectedRoute>}
          />
          <Route
              path="/subjects/:subjectId/topics"
              element={
                  <ProtectedRoute>
                      <TopicsPage/>
                  </ProtectedRoute>
              }
          />
          <Route
              path="/topics/:topicId/flashcards"
              element={
                  <ProtectedRoute>
                      <FlashcardsPage />
                  </ProtectedRoute>
              }
          />
          <Route
            path="/topics/:topicId/quiz"
            element={
                <ProtectedRoute>
                    <QuizPage />
                </ProtectedRoute>
            }
          />
          <Route
              path="/topics/:topicId/quiz/history"
              element={
                  <ProtectedRoute>
                      <QuizHistoryPage />
                  </ProtectedRoute>
              }
          />
          <Route
              path="/quizzes/:quizId"
              element={
                  <ProtectedRoute>
                      <QuizDetailPage />
                  </ProtectedRoute>
              }
          />
      </Routes>
  );
}

export default App;