//1997.10.27
#ifndef CWORD_H_
#define CWORD_H_

#include "WordInfo.h"

extern LPCTSTR word_attr[];

#define SENTENCE_LEN 100

enum {CHINESE_CHAR, ENGLISH_CHAR, DIGIT_CHAR, SYMBOL_CHAR, DOT_CHAR, SPECIAL_DIGIT, SPECIAL_CHAR, MOUSE_CHAR};

class CWord
{
// �Үm�ק諸����
public:
	// �H�U�o����ܼƬO���F�N�_���t�ΩҸ��J�����w��X�ҳ]�p
	WORD_DB *m_word_data;			// �bInitWord���A�|�N����Vword_data�R�A�ܼ�
	unsigned short *m_word_index;	// �bInitWord���A�|�N����Vword_index�R�A�ܼ�
// �H�U�����L���Ǫ���l���{���X
public:
	CWord();
	~CWord();

	void InitWord(LPCTSTR dir);
	void GetWord();
	void SetCharType();
	unsigned GetPhone(int ptr);
	void Score(int cur_ptr);
	void ChangePhone();
	int ReadText(FILE* fp);
	int GetSentence(UCHAR * from, int *textNdx);
	int IsNumberic(unsigned char *ch );
	void SetTone(int word_ndx, int char_ndx, USHORT new_tone);
public:
	int txt_len;
	int m_punctuation;
	unsigned char txt[SENTENCE_LEN*2];	// �o�����O���ҭn�B�z���y�l
	WORD_INFO w_info[80];				// �����A�j���X�A�o���N�X����
	int wnum;							//	word num�A�]�N�O�o�ӥy�l�Q�_���X�ӵ�

	int tab[SENTENCE_LEN][11];
	int ptrtab[SENTENCE_LEN][11];
	int best[SENTENCE_LEN];
	int q[SENTENCE_LEN*2];
	int toneComb[SENTENCE_LEN*2], toneComb4[SENTENCE_LEN*2];	//toneComb�PtoneComb4���t�O�n���O�A�@�ӬO���n�աA�@�ӬO�|�n��
	
	int voicedType[SENTENCE_LEN*2];			//���n�εL�n���l��
	int sentenceToneCobm[SENTENCE_LEN*2];

	int best_score;
	int char_type[SENTENCE_LEN+1];
    BOOL m_init;
};
#endif