#include "stdafx.h"
#include "Word.h"
#include "Phone.h"
#include "Memio.h"

int wordPitchNdx[4]={0, 5, 55, 430}; //5, 5*5*2=50, 5*5*5*3=375

static USHORT word_index_boundry=0;
static unsigned short /**phone_tab=NULL,*/*word_index=NULL ;
static WORD_DB *word_data;
static int init=0;
//#include "sui.h"
#define WORD_FILE "word.dat"
#define WORD_INDEX_FILE "word.ndx"
#define PHONE_FILE "phone.dat"

/* chinese symbal A1 40 ~ A1 7E  */
static unsigned char vo_class1[22][3] = {
       "  ","�t","�u","�v","�w","�x","�y","�z","�{","�|","�}","�~","��","��",
       "��","��","��","��","��","��","��","��" } ;
static unsigned char vo_class2[4][3] = {"","��","��","��"} ;
static unsigned char vo_class3[14][3] = {
       "","��","��","��","��","��","��","��","��","��","��","��","��","��" } ;
static unsigned char vo_class4[5][3] = {"�@","��", "��", "��", "��" };
static unsigned char symbol[96][3] = {"�@","�I","��","��","�C","�H","��","��"
       ,"�]","�^","��","��","�A","��","�D","��","��","��","��","��","��","��"
       ,"��","��","��","��","�G","�F","��","��","��","�H","�I","��","��","��"
       ,"��","��","��","��","��","��","��","��","��","��","��","��","��","��"
       ,"��","��","��","��","��","��","��","��","��","�e","�@","�f","�s","��"
       ,"��","��","��","��","��","��","��","��","��","��","��","��","��","��"
       ,"��","��","��","��","��","��","��","��","��","��","��","��","��","�a"
       ,"�U","�b","��","��" } ;


unsigned char numberic[][3] = {"�s","�@","�G","�T","�|","��","��","�C","�K","�E","�Q","��","�d","�U","��","��","�b","�X","�h","��"
     ,"��","�L","��","�v","��","��","�m","��","�h","�B","��","�a","��"} ;
static int num = 33;

static unsigned char tail_symbol[20] = "�A�C�I�H�J�F" ; //"�D"


CWord::CWord()
{
	m_init=FALSE;
}

CWord::~CWord()
{
	if(m_init) {
		init--;
	}
	if(init==0) {
		if( ttsPHONETAB != NULL ) FreeMem(ttsPHONETAB) ;
		ttsPHONETAB=NULL;

		if( word_index != NULL ) FreeMem( word_index) ;
		word_index=NULL;

		if( word_data != NULL ) FreeMem( word_data) ;
		word_data=NULL;
	}
}

void CWord::InitWord(LPCTSTR dir)
{
	CCFile fp;
	CString cs;
	int len;

	init ++;
	m_init=TRUE;
	if(init>1) return;
	word_data = NULL;
	word_index=NULL;

	cs.Empty();
	cs=cs+dir+WORD_FILE;
	fp.Copen(cs);
	len = (int)fp.GetLength() ;
	int aa = sizeof(WORD_DB);
	int bb = sizeof(char);
	int cc = sizeof(UCHAR);
	int dd = sizeof(UINT);
	int ee = sizeof(short);
	word_data = (WORD_DB*) AllocMem( len );// quit("memory not enough") ;
	fp.Cread( word_data,len) ;
	fp.Cclose();

	cs.Empty();
	cs=cs+dir+PHONE_FILE;
	
	/*****************************fable 20080527*********************/
	/*start         ���}�ɮɥ�new  �R���ɥ� FreeMem (�P�[�c)      */
	/*****************************fable 20080527*********************/
	/*
	ttsPHONETAB=(short*)LoadFile(cs, 1, NULL);/**/
	fp.AliveOpen(cs);
	len=(int)fp.GetLength();
	ttsPHONETAB=(short*)AllocMem( len );
	fp.Cread(ttsPHONETAB,len);
	fp.Close();
	/*****************************fable 20080527*********************/
	/*end
	/*****************************fable 20080527*********************/

	cs.Empty();
	cs=cs+dir+WORD_INDEX_FILE;
	fp.Copen(cs);
	len = (int)fp.GetLength() ;
	word_index = (unsigned short*) AllocMem( len );// quit("memory not enough") ;
	fp.Cread( word_index,len) ;
	fp.Cclose() ;
	//::GetCurrentDirectory(256,tts_output_dir);
	int i,j;

	j=0;
	for(i=1; i<CHINESE_INDEX_NUM; i++) {
		if(word_index[i]==0xffff) continue;
		if(word_index[i]<j) break;
		j=word_index[i];
	}	
	word_index_boundry=i;

	// �Үm�[�J�ק諸����
	m_word_data = word_data;
	m_word_index = word_index;
}


void CWord::GetWord()
{
	//static unsigned char word[40] ;
	int i,j,k,start, ptr, offset;
	long ndx;
	UCHAR *leading;
	WORD_DB *wdb;
	BOOL found;

	// text_len�O�HUnicode�p��
	for ( i = 0 ; i < txt_len+1 ; i ++ ) {
		// SENTENCE_LEN�w�q��100
		// ��tab���ŧi�覡�� int tab[SENTENCE_LEN][11]
		tab[i][0] = 1 ;
		tab[i][1] = i ;
		// �H�Uptrtab���ŧi�覡��int ptrtab[SENTENCE_LEN][11]
		ptrtab[i][1] = -1 ;
    }
	// tab�|�����p�U�ҥ�
	// 1 0 0 0 0 0 0 0 0 0 0
	// 1 1 0 0 0 0 0 0 0 0 0
	// 1 2 0 0 0 0 0 0 0 0 0
	// 1 3 0 0 0 0 0 0 0 0 0
	// ...
	// �@��text_len��row
	// ptrtab�|�����p�U�ҥ�
	// - -1 0 0 0 0 0 0 0 0 0
	// - -1 0 0 0 0 0 0 0 0 0
	// - -1 0 0 0 0 0 0 0 0 0
	// - -1 0 0 0 0 0 0 0 0 0
	// ...
	// �@��text_len��row

	// �H�U�^��O�B�z�b��J���y�l���r�ƥH�~�A��SENTENCE_LEN����tab[][]�����e
	for( ; i< SENTENCE_LEN ; i ++ ) tab[i][0] = 0 ;


	// �]�wint char_type[SENTENCE_LEN+1]�����e
	// char_type�����e�i�H��CHINESE_CHAR, ENGLISH_CHAR, DIGIT_CHAR�����A�p�U�ҦC
	// enum {CHINESE_CHAR, ENGLISH_CHAR, DIGIT_CHAR, SYMBOL_CHAR, DOT_CHAR, SPECIAL_DIGIT, SPECIAL_CHAR, MOUSE_CHAR};
	SetCharType();

	// ptr�O���禡���ŧi���ϰ��ܼơA���A��int
	// ptr�b�C���j�餤���[2�A�O�]��Unicode�s�X�����Y
	// ��text_len�n���G����]�]�O
	for( ptr = 0 ; ptr < txt_len * 2 ; ptr +=2 ){
		// found�]�O�ϰ��ܼơA���A��BOOL
		found=FALSE;
		// �p�G��J�r�ꪺ���e����"�D"�A�N�N�L����"�I"�A�ç�������char_type[]
		if(memcmp(&txt[ptr],"�D",2) ==0 ){
			memcpy(&txt[ptr],"�I",2);
			char_type[ptr/2]=CHINESE_CHAR;
		}
		if(txt[ptr]>0xF9 || txt[ptr]<0xA4) continue;
		// ndx���ϰ��ܼơA�@��word��index�A���A�������long
		ndx = ( *(txt+ptr) - 0xa4 ) * 157 ;
		ndx+= ( *(txt+ptr+1) > 0xa0 ) ? *(txt+ptr+1) - 0xa1 + 0x3f : *(txt+ptr+1) - 0x40 ;

		// leading���ϰ��ܼƫ��A��UCHAR*�]�N�Ounsigned char*
		leading=NULL;
		if ( word_index[ ndx ] != 0xffff ){
			offset = word_index[ndx] ;
			if(ndx >= word_index_boundry) offset += 65535L;
			for( i = offset ; ; i++ ){
				// �ѵ��w��Ū�X�H��J�r�ꤤ�ثeindex�쪺�r�������Ҧ�����
				// word_data�����w�A���Ь�ndx
				wdb=&word_data[i];
				if(leading!=NULL && (leading[0]!=wdb->big5[0] || leading[1]!=wdb->big5[1]))
				   break;
				leading=wdb->big5;
				j=wdb->byte;

				// �u�B�z������̤j���|�r��
				if(j>2*4) continue;

				// �ھڵ���j�A����O�_�ۦP�A�^�ǭȭY���s�A��ܤ@�Ҥ@��
				if( memcmp( txt+ptr, wdb->big5, j) == 0 ) {
					start = ptr /2 ;
					int k;
					if(j==2) {
						k=1;
						tab[start][1] = start;
					} else {
						k=++tab[start][0];
					}
					tab[start][k] = start + j / 2  -1 ;
					ptrtab[start][k] = i ;
					found=TRUE;
                }
            }
        }
    }
	short *p;
	WORD_INFO *pwi;
	WORD_DB *pwdb;

	memset(w_info,0,sizeof(w_info));
	start = wnum= ndx=0;
	best_score = -9999 ;
	Score(0) ;
	
	while(1) {
		pwi=&w_info[wnum];
		pwi->sen_pos=1; //�����]���b�y�l������
		//UCHAR uc=txt[ndx*2];
		
		if(ptrtab[start][best[start]]>=0) {
			pwdb=&word_data[ptrtab[start][best[start]]];	
			memcpy(pwi->attr,pwdb->attr,4);
		} else memset(pwi->attr,0,4);

		if( tab[ start][best[start]] == start ) {
			//memset(pwi->attr,0,4);
			pwi->phone[0] = GetPhone(start) ;
			memcpy(pwi->big5,&txt[ndx*2],2);
			pwi->wlen=1;
			ndx++;
		} else {
			
			//memcpy(pwi->attr,pwdb->attr,4);

			p=pwdb->phone;
			j = tab[start][best[start]] - start + 1;
			memcpy(pwi->big5,pwdb->big5,j*2);
			pwi->wlen=j;
			ndx+=j;
			for( i = 0 ; i < j ; i ++ ) {
				pwi->phone[i] = p[i] ;
			}
		}
		wnum ++;
		start = tab[ start][ best[start] ] + 1 ;
		if( start >= txt_len ) break  ;
	}
	if(char_type[txt_len/2]==CHINESE_CHAR && w_info[wnum-1].phone[0]>=SD_PUNC) {
		pwi->sen_pos=2;
		if(wnum>2) {
			w_info[0].sen_pos=0;
			w_info[wnum-2].sen_pos=2;
		}
	} else if(wnum>1) {
		w_info[0].sen_pos=0;
		pwi->sen_pos=2;
	}
	ChangePhone();

	start=0;
	int t,t1, t2, t3;
	t1=t2=t3=0;
	m_punctuation=0;
	for(i=0; i<wnum; i++) {
		pwi=&w_info[i];
		toneComb4[i]=toneComb[i]=0;
		for(k=start, j=0; j<pwi->wlen; j++ ,k++) {
			if(char_type[k]==CHINESE_CHAR && pwi->phone[j]<SD_PUNC) {
				t=Tone(pwi->phone[j])-1;
				toneComb[i] = toneComb[i]*5 + t;
				if(t==4) toneComb4[i]=-100000;
				else toneComb4[i] = toneComb4[i]*4 + t;
			} else {
				toneComb4[i]=toneComb[i]=-100000;
				m_punctuation=1;
				break;
			}
		}
		
		for(j=0, k=start; j<pwi->wlen; j++,k++ ) {//����P�W�����X�֡G�]���� break;
			t3=Tone(pwi->phone[j]);
			if( t3==TONEBAD || t3<1 || t3>5){
				t1=t2=t3=0;
				voicedType[k]=0;
			} else {
				t3--;
				voicedType[k]=VoicedType(pwi->phone[j]);
			}
			sentenceToneCobm[k]=t3 + t2*5 +t1*25;
			t1=t2;
			t2=t3;
		}
		start += pwi->wlen;
	}
}

void CWord::Score(int cur_ptr)
{
	short count ,start;
	short i ;

	if( cur_ptr >= txt_len ) {
		count = 9000 , start = 0 ;
        while(1) {
			if( tab[start][q[start]] == start )count -=20 ;
            else count -= 10;
            start = tab[ start][ q[start] ] + 1 ;
            if( start >= txt_len ) break  ;
        }
        if( count >= best_score ) {
            best_score = count  ;
            for( i = 0 ; i < txt_len ; i ++ )  {
				best[i] = q[i] ;
            }
        }
        return ;
	}
	for( i = 1 ; i <= tab[cur_ptr][0] ; i ++ ) {
		q[cur_ptr] = i ;
		Score( tab[cur_ptr][i] + 1);
	}
}

void CWord::SetCharType()
{
	UINT v;
	UCHAR *p;
	int i,j;

	for(i=0; i<txt_len; i++){
		p=&txt[i*2];
		v=p[0]*256+p[1];
		if(v>=0xa2af && v<=0xa2b8) { //��������������������
			char_type[i]=DIGIT_CHAR;
			strncpy((char *)&txt[2*i],(char*)&numberic[v-0xa2af][0],2);
		} else if(v>=0xa2cf && v<=0xa2fe) { //�ϢТѡD�D�D��
			char_type[i]=ENGLISH_CHAR;
		} else if(v==0xa144){  //  .
			char_type[i]=DOT_CHAR;
		} else if(v==0xa249){
			char_type[i]=MOUSE_CHAR;
		} else char_type[i]=CHINESE_CHAR;
	}

	//the following is for email IP: @ must be in IP==> utter as Mrs White
	//if ".." ??
	for(i=0; i<txt_len; i++){
		if(char_type[i]==DOT_CHAR){
			if(i && i<txt_len-1 && 
			   (char_type[i-1]==ENGLISH_CHAR ||char_type[i-1]==DIGIT_CHAR || char_type[i-1]==SPECIAL_DIGIT)&&
															   //^^^^^^^^SPECIAL_DIGIT ??
			   (char_type[i+1]==ENGLISH_CHAR ||char_type[i+1]==DIGIT_CHAR)
			  ) {
				strncpy((char*)&txt[2*i],"�I",2);
				for(j=i-1; j>=0; j--){
					if(char_type[j]==ENGLISH_CHAR || char_type[j]==DIGIT_CHAR){
						if(char_type[j]==DIGIT_CHAR) char_type[j]=SPECIAL_DIGIT;
					} else break;
				}
				for(i++; i<txt_len; i++){
					if(char_type[i]==ENGLISH_CHAR || char_type[i]==DIGIT_CHAR){
						if(char_type[i]==DIGIT_CHAR) char_type[i]=SPECIAL_DIGIT;
					} else {
						i--;
						break;
					}
				}
			} else char_type[i]=SYMBOL_CHAR;
	    }
	}
}


unsigned CWord::GetPhone(int ptr)
/* return 0 if not a voice_able  word */
/* return 1 ~ 27965 if a chinese voice_able word */
/* return 30000 if a english word */
{
	UCHAR buf[22];
	//unsigned best=0,offset ;
	SNDID sid[10];

	buf[0]=txt[ptr*2];
	buf[1]=txt[ptr*2+1];
	buf[2]=0;
	if(buf[0]<0xa4 || buf[0]>0xf9) return SD_PUNC;
	// buf�O�_�X�Ӫ��@�ӭӪ���
	// �Ҧp"�ڬ��H�H�A�H�H����"����"��"
	Big52SID(buf,sid);
	return sid[0];
}

int CWord::ReadText(FILE* fp)   /* return 0 if end of file */
{
  unsigned char ch[3], tmp;
  int len = 0 ;
  char *p ;
  BOOL mouse;
  int dot;
  unsigned char* buf=txt;

  mouse = FALSE;
  dot=0;
  ch[2]=0;
  while( fread( ch,1,1,fp) == 1 ) {
     if( ch[0] == 0x1A ) {
        if( len == 0 ) len=-1 ;
		goto ret;
        }
     if( ch[0] == 0x0D ) {
         if( (ch[1] = (UCHAR)fgetc(fp)) != 0x0A ) AfxMessageBox("error: 0xd not followed 0xa")  ;
         /*if( len == 0 ) continue ;
         buf[len*2] = 0xA1 ;
         buf[len*2+1] = 0x40 ;
         len ++ ;
         goto ret ;*/
		 continue;
        }
     if( ch[0] < 128 ) {     /* English letter */
         if(ch[0]<0x20) ch[0] = 0x20 ;
		 tmp=ch[0];
		 if(tmp=='@') {
			 dot=0;
			 mouse=TRUE;
		 } else if(tmp=='.') {
			 dot++;
			 if(dot>=6) {
				 dot=0;
				 mouse=FALSE;
			 }
		 }
         memcpy( &buf[len*2], symbol[ch[0]-0x20],2 ) ;
         memcpy( ch, &buf[len*2],2) ;
         ch[2] = 0 ;
		 /*if(len && ch[0]==0xa1 && ch[1]==0x40 && buf[len*2-2]==0xa1 && buf[len*2-1]==0x40)
			continue;*/
		 if(ch[0]==0xa1 && ch[1]==0x40)
			continue;
		 if(tmp!='.' || !mouse) {
			p=strstr( (char*)tail_symbol,(char*)ch ) ;
			if( (p!= NULL) && ( ((char*)p-(char*)tail_symbol)%2 == 0) ) {len++; goto ret;}
		
            if(len > WORD_LEN2*2 ) if( buf[len*2] == 0xA1 )
				if((buf[len*2+1]>= 0x40) &&(buf[len*2+1] <= 0x7F) ) {  /* symbol */
                   len++ ;
				   goto ret;
				}
		 }
         len ++ ;
		 if(len>WORD_LEN2*2) goto ret;
         continue ;
         }
	 dot=0;
	 mouse=FALSE;
     ch[1] = (UCHAR)fgetc(fp) ;
////*********
	 if(memcmp(ch,"��",2)==0) {
		 memcpy(&buf[len*2],"�T�Q",4);
		 len++;
	 } else if(memcmp(ch,"��",2)==0) {
		 memcpy(&buf[len*2],"�G�Q",4);
		 len++;
	 } else {
		memcpy( &buf[len*2],ch,2) ;
	 }
     ch[2] = 0 ;
	 if(len && ch[0]==0xa1 && ch[1]==0x40 && buf[len*2-2]==0xa1 && buf[len*2-1]==0x40)
		continue;
     p=strstr( (char*)tail_symbol,(char*)ch ) ;
     if(p!= NULL) {
		 if(memcmp(p,"�D",2)==0) {
			 memcpy( &buf[len*2],"�I",2) ;
		 } else if( ((char*)p-(char*)tail_symbol)%2 == 0) {
			  len++ ; 
			  goto ret;
		  }
	 }
	 
     if( len > WORD_LEN2*2 ) if( buf[len*2] == 0xA1 )
         if((buf[len*2+1]> 0x40) &&(buf[len*2+1] < 0x7F) )   /* symbol */
		 {len++; goto ret;}
     len ++ ;
	 if(len>WORD_LEN2*2) goto ret;
     }
ret:
	 txt_len=len;
	 if(len>0) {
		 buf[len*2] = 0 ;
		 return len;
	 }
	 return (-1) ;
}

int CWord::GetSentence(UCHAR * from, int *textNdx)   /*return 0 if end*/
{
  unsigned char ch[3], tmp;
  int len = 0 ;
  char *p ;
  BOOL mouse;
  int dot;
  unsigned char* buf=txt;

  mouse = FALSE;
  dot=0;
  ch[2]=0;
  while( (ch[0]=from[(*textNdx)++])!=0 ) {
     if( ch[0] == 0x1A ) {
        if( len == 0 ) len=-1 ;
        goto ret;
        }
     if( ch[0] == 0x0D ) {
         if( (ch[1] = from[(*textNdx)++]) != 0x0A ) AfxMessageBox("error: 0x0d not followed 0x0a")  ;
		 continue;
        }
     if( ch[0] < 128 ) {     /* English letter */
         if(ch[0]<0x20) ch[0] = 0x20 ;
		 tmp=ch[0];
         memcpy( &buf[len*2], symbol[ch[0]-0x20],2 ) ;
         memcpy( ch, &buf[len*2],2) ;
         ch[2] = 0 ;

 		 if(ch[0]==0xa1 && ch[1]==0x40)
			continue;

		 //if(tmp!='.' || !mouse) {
			p=strstr( (char*)tail_symbol,(char*)ch ) ;
			if( (p!= NULL) && ( ((char*)p-(char*)tail_symbol)%2 == 0) ) {len++ ; goto ret;}
		
            if(len > WORD_LEN2 ) if( buf[len*2] == 0xA1 )
               if((buf[len*2+1]>= 0x40) &&(buf[len*2+1] <= 0x7F) )   /* symbol */
			   {len++; goto ret; }
		 //}
         len ++ ;
		 if(len>WORD_LEN2) goto ret;
         continue ;
         }
	 dot=0;
	 mouse=FALSE;
     ch[1] = from[(*textNdx)++] ;
////*********
	 if(memcmp(ch,"��",2)==0) {
		 memcpy(&buf[len*2],"�T�Q",4);
		 len++;
	 } else if(memcmp(ch,"��",2)==0) {
		 memcpy(&buf[len*2],"�G�Q",4);
		 len++;
	 } else {
		memcpy( &buf[len*2],ch,2) ;
	 }
     ch[2] = 0 ;
	 if(len && ch[0]==0xa1 && ch[1]==0x40 && buf[len*2-2]==0xa1 && buf[len*2-1]==0x40)
		continue;
     p=strstr( (char*)tail_symbol,(char*)ch ) ;
     if(p!= NULL) {
		 if(memcmp(p,"�D",2)==0) {
			 memcpy( &buf[len*2],"�I",2) ;
		 } else if( ((char*)p-(char*)tail_symbol)%2 == 0) {
			  len++ ; 
			  goto ret;
		  }
	 }

     if( len > WORD_LEN2*2 ) if( buf[len*2] == 0xA1 )
         if((buf[len*2+1]> 0x40) &&(buf[len*2+1] < 0x7F) )   /* symbol */
		 {len++; goto ret;}
     len ++ ;
	 if(len>WORD_LEN2*2) goto ret;
  }

  (*textNdx)--;
ret:
  	 txt_len=len;
	 if(len>0) {
		 buf[len*2] = 0 ;
		 return len;
	 }
	 return (-1) ;
}

int CWord::IsNumberic(unsigned char *ch )
{
 int i ;

 for( i = 0 ;i < num ;i ++ ) {
       if( memcmp( numberic[i],ch, 2 ) == 0 ) break ;
       }
 if( i != num ) return (1) ;
 else return(0) ;
}

void CWord::SetTone(int wno, int ndx, USHORT new_tone)
{
	w_info[wno].phone[ndx] = w_info[wno].phone[ndx]/10*10 +new_tone;
}

BOOL CCsame(UCHAR * s, const int code); //one Chinese Character Comparison
BOOL CCsame(UCHAR * s, const int code)
{
/*	int i=(s[0]<<8) + s[1];
	return (i==code);
*/
	_asm {
		mov eax, dword ptr [s]
		mov eax,[eax]
		and eax,0xffff
		xchg ah,al
		cmp  eax,dword ptr [code]
		je   same
	}
	return 0;
same:
	return 1;
}

char *Pohin[][2]={
	{"��" , "�x����"  },
	{"��" , "������"},
	{"��" , "������"  },
	{"��" , "�~����" },
	{"��" , "�|��" },
	{"��" , "�x����" },
	{"��" , "������"},
	{"��" , "��������"},
	{"��" , "��������"},
   {"�B" , "������"   },
   {"��" , "����"},
   {"��" , "������"  }, 
   {"��" , "��������"},
	{"��" , "�x����"  },
	{"�l" , "����"    },
	{"��" , "��������"},
	{"��" , "��������"}, 
	{"��" , "�}����"  }, //num
	{"�J" , "��������"},//num
	{"��" , "����"    },//num
	{"��" , "������"},
	{"�b", "������"},
   {"�R" , "�y����"},
   {"�b" , "������"  },
   {"��" , "��������"},
   {"��" , "������"},
   {"��" , "��������"},
   {"�M" , "������"  },
   {"�[" , "�~����"  },
   {"ı" , "��������"},
   {"�c" , "������"},
   {"��" , "��������"},
   //{"��" , "�w��"},
   //{"��" , "�u��"    , "�u����"  },
	{NULL,NULL}
};
/**/
/**/
/**************************************2007 09 19 fable*****/
/*���]��Ʈw����ƥ��������T�����						   */
/*�ҥH���_�X�Ӫ������A��sandhi rule������				   */
/*�u�w����P��������sandhi rule���ഫ					   */
/**************************************2007 09 19 fable*****/

void CWord::ChangePhone()
{
	int /*i,*/n,tone,prev_tone;
	WORD_INFO *pwi/*, *next_pwi*/;
	UCHAR *first_char;
	UCHAR tmp=0;
	bool flag;//for �@

	prev_tone=-1;
	flag=false;
	first_char=&tmp;
	for(n=wnum-1; n>=0; n--)
	{
		pwi=&w_info[n];
		//3+3 rule between phones
		tone=Tone(pwi->phone[pwi->wlen-1]);
		if(prev_tone==3 && tone==3) 
		{
			SetTone(n,pwi->wlen-1,2);
		}

		if(pwi->wlen==1)
		{
			//�Ʀr+ �� ��
			if(IsNumberic(pwi->big5))
			{
				if( CCsame( first_char,'��') )
				{
					SetTone(n+1,0,3);
					prev_tone=3;
				}
				if( CCsame( first_char,'��') )
				{
					SetTone(n+1,0,5);
					prev_tone=5;
				}
			}						
			//�@'s rule
			if(flag)//�Ʀr �D �� �� �U �d ��+�@
			{
				if(IsNumberic(pwi->big5))
					SetTone(n+1,0,1);
				flag=false;
			}
			
			if( CCsame( pwi->big5,'�@') )
			{
				//�@�b�̫��
				if(n==wnum-1)
				{
					SetTone(n,0,1);
					continue;
				}
				//+(4 || 5)=>����      ex �@�� �@��
				//+(1 || 2 || 3)=>���� ex �@�� �@�H �@��
				if(prev_tone==4 || prev_tone==5)SetTone(n,0,2);
				else SetTone(n,0,4);

				if(!IsNumberic(first_char))
				{
					flag=true;//�аO�W�@�Ӧr���@+�D�Ʀr�ɪ����p �ΥH�Ʀr+�@+�D�Ʀr�ɧ��
				}
				else//�@�᭱�O�Ʀr��  �D �� �� �U �d ��
				{
					if(CCsame( first_char,'��') || CCsame( first_char,'��') || CCsame( first_char,'�U') || CCsame( first_char,'�d') || CCsame( first_char,'��'))
						continue;
					SetTone(n,0,1);
					flag=false;
				}
			}
			//��
			//+(4)=>�t����
			//+(1 || 2 || 3)=> �t����
			if( CCsame( pwi->big5,'��') )
			{
				if(prev_tone==4 || prev_tone==5)SetTone(n,0,2);
				else SetTone(n,0,4);
			}
			if( CCsame( pwi->big5,'��') )
			{
				SetTone(n,0,1);
			}
			if( CCsame( pwi->big5,'��') )
			{
				SetTone(n,0,4);
			}
			if(CCsame(first_char,'��'))
			{
				if (CCsame(pwi->big5,'��') ||CCsame(pwi->big5,'��') )
				{
					SetTone(n+1,0,3);
				}
			}
		}

		first_char=&pwi->big5[0];
		prev_tone=Tone(pwi->phone[0]);
	}
}
/**************************************2007 09 19 fable*****/
