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
import ChatPage from './pages/ChatPage';
import Layout from './components/Layout';
import StudyPlanPage from './pages/StudyPlanPage';
import StudySessionPage from './pages/StudySessionPage';

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
                      <Layout>
                          <DashboardPage />
                      </Layout>
                  </ProtectedRoute>
              }
          />
          <Route
              path="/subjects/:subjectId/topics"
              element={
                  <ProtectedRoute>
                      <Layout>
                          <TopicsPage />
                      </Layout>
                  </ProtectedRoute>
              }
          />
          <Route
              path="/topics/:topicId/flashcards"
              element={
                  <ProtectedRoute>
                      <Layout>
                          <FlashcardsPage />
                      </Layout>
                  </ProtectedRoute>
              }
          />
          <Route
              path="/topics/:topicId/quiz"
              element={
                  <ProtectedRoute>
                      <Layout>
                          <QuizPage />
                      </Layout>
                  </ProtectedRoute>
              }
          />
          <Route
              path="/topics/:topicId/quiz/history"
              element={
                  <ProtectedRoute>
                      <Layout>
                          <QuizHistoryPage />
                      </Layout>
                  </ProtectedRoute>
              }
          />
          <Route
              path="/quizzes/:quizId"
              element={
                  <ProtectedRoute>
                      <Layout>
                          <QuizDetailPage />
                      </Layout>
                  </ProtectedRoute>
              }
          />
          <Route
              path="/topics/:topicId/chat"
              element={
                  <ProtectedRoute>
                      <Layout>
                          <ChatPage />
                      </Layout>
                  </ProtectedRoute>
              }
          />
          <Route
              path="/subjects/:subjectId/study-plan"
              element={
                  <ProtectedRoute>
                      <Layout>
                          <StudyPlanPage />
                      </Layout>
                  </ProtectedRoute>
              }
          />
          <Route
              path="/topics/:topicId/study-session"
              element={
                  <ProtectedRoute>
                      <Layout>
                          <StudySessionPage />
                      </Layout>
                  </ProtectedRoute>
              }
          />
      </Routes>


  );
}

export default App;