// Learning Module Type Definitions

export interface LearningObjective {
  id: string;
  title: string;
  description: string;
  completed: boolean;
}

export interface QuizQuestion {
  id: string;
  type: 'multiple-choice' | 'true-false' | 'fill-blank' | 'drag-drop' | 'code';
  question: string;
  options?: string[];
  correctAnswer: string | string[];
  explanation: string;
  points: number;
  difficulty: 'easy' | 'medium' | 'hard';
  hints?: string[];
}

export interface InteractiveElement {
  id: string;
  type: 'simulation' | 'diagram' | 'code-editor' | 'video' | 'animation' | 'virtual-lab';
  title: string;
  content: any;
  config?: Record<string, any>;
}

export interface ContentBlock {
  id: string;
  type: 'text' | 'heading' | 'image' | 'video' | 'code' | 'interactive' | 'quiz';
  content: string | InteractiveElement | QuizQuestion;
  metadata?: {
    estimatedTime?: number;
    difficulty?: 'easy' | 'medium' | 'hard';
    tags?: string[];
  };
}

export interface LearningPath {
  id: string;
  title: string;
  description: string;
  prerequisiteModules?: string[];
  modules: string[];
  estimatedDuration: number;
  difficulty: 'beginner' | 'intermediate' | 'advanced';
}

export interface StudentProgress {
  moduleId: string;
  studentId: string;
  completedObjectives: string[];
  currentSection: number;
  quizScores: Record<string, number>;
  timeSpent: number;
  lastAccessed: Date;
  notes: string[];
  bookmarks: string[];
}

export interface LearningModule {
  id: string;
  title: string;
  description: string;
  subject: string;
  level: 'beginner' | 'intermediate' | 'advanced';
  estimatedDuration: number; // in minutes
  learningObjectives: LearningObjective[];
  sections: LearningSection[];
  finalAssessment?: QuizQuestion[];
  resources: Resource[];
  tags: string[];
  createdBy: string;
  createdAt: Date;
  updatedAt: Date;
  isPublished: boolean;
  thumbnail?: string;
  prerequisites?: string[];
}

export interface LearningSection {
  id: string;
  title: string;
  description: string;
  order: number;
  content: ContentBlock[];
  quiz?: QuizQuestion[];
  estimatedTime: number;
  isRequired: boolean;
}

export interface Resource {
  id: string;
  title: string;
  type: 'pdf' | 'link' | 'video' | 'audio' | 'document';
  url: string;
  description?: string;
  size?: string;
}

export interface GameElement {
  type: 'points' | 'badges' | 'leaderboard' | 'progress-bar' | 'achievements';
  config: Record<string, any>;
}

export interface AdaptiveLearning {
  enabled: boolean;
  difficultyAdjustment: boolean;
  personalizedContent: boolean;
  learningStyleAdaptation: 'visual' | 'auditory' | 'kinesthetic' | 'mixed';
}

export interface ModuleSettings {
  allowSkipping: boolean;
  requireSequentialProgress: boolean;
  showProgress: boolean;
  enableNotes: boolean;
  enableBookmarks: boolean;
  timeTracking: boolean;
  gamification?: GameElement[];
  adaptiveLearning?: AdaptiveLearning;
}

export interface ModuleAnalytics {
  totalStudents: number;
  completionRate: number;
  averageScore: number;
  averageTimeSpent: number;
  difficultSections: string[];
  commonMistakes: Record<string, number>;
  engagementMetrics: {
    dropOffPoints: string[];
    mostViewedSections: string[];
    leastViewedSections: string[];
  };
}
